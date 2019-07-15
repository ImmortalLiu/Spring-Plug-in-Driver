package com.fiicloud.plugindriver.core.builder;

import com.fiicloud.plugindriver.core.base.BaseBuilder;
import com.fiicloud.plugindriver.core.base.BaseConfigurer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class AbstractConfiguredPlugInDriverBuilder<O, B extends BaseBuilder<O>> extends AbstractPlugInDriverBuilder<O> {
    private Log logger;
    private final LinkedHashMap<Class<? extends BaseConfigurer<O, B>>, List<BaseConfigurer<O, B>>> configurers;
    private final List<BaseConfigurer<O, B>> configurersAddedInInitializing;
    private final boolean allowConfigurersOfSameType;
    private AbstractConfiguredPlugInDriverBuilder.BuildState buildState;

    protected AbstractConfiguredPlugInDriverBuilder(boolean allowConfigurersOfSameType) {
        this.logger = LogFactory.getLog(this.getClass());
        this.configurers = new LinkedHashMap<>();
        this.configurersAddedInInitializing = new ArrayList<>();
        this.allowConfigurersOfSameType = allowConfigurersOfSameType;
    }

    public <C extends BaseConfigurer<O, B>> C apply(C configurer) throws Exception {
        this.add(configurer);
        return configurer;
    }

    private <C extends BaseConfigurer<O, B>> void add(C configurer) throws Exception {
        Assert.notNull(configurer, "configurer cannot be null");
        Class<? extends BaseConfigurer<O, B>> clazz = (Class<? extends BaseConfigurer<O, B>>) configurer.getClass();
        synchronized(this.configurers) {
            if (this.buildState.isConfigured()) {
                throw new IllegalStateException("Cannot apply " + configurer + " to already built object");
            } else {
                List<BaseConfigurer<O, B>> configs = this.allowConfigurersOfSameType ? (List)this.configurers.get(clazz) : null;
                if (configs == null) {
                    configs = new ArrayList(1);
                }

                ((List)configs).add(configurer);
                this.configurers.put(clazz, configs);
                if (this.buildState.isInitializing()) {
                    this.configurersAddedInInitializing.add(configurer);
                }

            }
        }
    }

    public <C extends BaseConfigurer<O, B>> List<C> getConfigurers(Class<C> clazz) {
        List<C> configs = (List)this.configurers.get(clazz);
        return configs == null ? new ArrayList() : new ArrayList(configs);
    }

    public <C extends BaseConfigurer<O, B>> List<C> removeConfigurers(Class<C> clazz) {
        List<C> configs = (List)this.configurers.remove(clazz);
        return configs == null ? new ArrayList() : new ArrayList(configs);
    }

    public <C extends BaseConfigurer<O, B>> C getConfigurer(Class<C> clazz) {
        List<BaseConfigurer<O, B>> configs = (List)this.configurers.get(clazz);
        if (configs == null) {
            return null;
        } else if (configs.size() != 1) {
            throw new IllegalStateException("Only one configurer expected for type " + clazz + ", but got " + configs);
        } else {
            return (C) configs.get(0);
        }
    }

    public <C extends BaseConfigurer<O, B>> C removeConfigurer(Class<C> clazz) {
        List<BaseConfigurer<O, B>> configs = (List)this.configurers.remove(clazz);
        if (configs == null) {
            return null;
        } else if (configs.size() != 1) {
            throw new IllegalStateException("Only one configurer expected for type " + clazz + ", but got " + configs);
        } else {
            return (C)configs.get(0);
        }
    }

    /**
     * 获取或构建
     * @return
     */
    public O getOrBuild() {
        if (this.isUnbuilt()) {
            try {
                return this.build();
            } catch (Exception var2) {
                this.logger.debug("Failed to perform build. Returning null", var2);
                return null;
            }
        } else {
            return this.getObject();
        }
    }

    /**
     * 判断configurers是否未构建
     * @return
     */
    private boolean isUnbuilt() {
        synchronized(this.configurers) {
            return this.buildState == AbstractConfiguredPlugInDriverBuilder.BuildState.UNBUILT;
        }
    }

    protected void beforeInit() throws Exception {
    }

    protected void beforeConfigure() throws Exception {
    }

    protected abstract O performBuild() throws Exception;

    private void init() throws Exception {
        Collection<BaseConfigurer<O, B>> configurers = this.getConfigurers();
        Iterator var2 = configurers.iterator();

        BaseConfigurer configurer;
        while(var2.hasNext()) {
            configurer = (BaseConfigurer)var2.next();
            configurer.init(this);
        }

        var2 = this.configurersAddedInInitializing.iterator();

        while(var2.hasNext()) {
            configurer = (BaseConfigurer)var2.next();
            configurer.init(this);
        }

    }

    private void configure() throws Exception {
        Collection<BaseConfigurer<O, B>> configurers = this.getConfigurers();

        for (BaseConfigurer<O, B> obBaseConfigurer : configurers) {
            obBaseConfigurer.configure((B) this);
        }

    }

    private Collection<BaseConfigurer<O, B>> getConfigurers() {
        List<BaseConfigurer<O, B>> result = new ArrayList<>();

        for (List<BaseConfigurer<O, B>> baseConfigurers : this.configurers.values()) {
            result.addAll(baseConfigurers);
        }

        return result;
    }


    @Override
    protected O doBuild() throws Exception {
        synchronized(this.configurers) {
            this.buildState = AbstractConfiguredPlugInDriverBuilder.BuildState.INITIALIZING;
            this.beforeInit();
            this.init();
            this.buildState = AbstractConfiguredPlugInDriverBuilder.BuildState.CONFIGURING;
            this.beforeConfigure();
            this.configure();
            this.buildState = AbstractConfiguredPlugInDriverBuilder.BuildState.BUILDING;
            O result = this.performBuild();
            this.buildState = AbstractConfiguredPlugInDriverBuilder.BuildState.BUILT;
            return result;
        }
    }

    private static enum BuildState {
        /**
         *未构建
         */
        UNBUILT(0),
        /**
         * 正在初始化
         */
        INITIALIZING(1),
        /**
         * 配置
         */
        CONFIGURING(2),
        /**
         * 正在构建
         */
        BUILDING(3),
        /**
         * 已构建
         */
        BUILT(4);

        private final int order;

        private BuildState(int order) {
            this.order = order;
        }

        public boolean isInitializing() {
            return INITIALIZING.order == this.order;
        }

        public boolean isConfigured() {
            return this.order >= CONFIGURING.order;
        }
    }

}
