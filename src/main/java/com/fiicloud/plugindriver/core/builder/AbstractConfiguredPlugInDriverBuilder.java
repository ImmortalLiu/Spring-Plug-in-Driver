package com.fiicloud.plugindriver.core.builder;

import com.fiicloud.plugindriver.core.configurer.BaseConfigurer;
import com.fiicloud.plugindriver.core.configurer.AbstractPlugInDriverConfigurerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author Anthony
 */
@SuppressWarnings("unchecked")
public abstract class AbstractConfiguredPlugInDriverBuilder<O, B extends BaseBuilder<O>> extends AbstractPlugInDriverBuilder<O> {
    private Log logger;
    /**
     * 配置类
     */
    private final LinkedHashMap<Class<? extends BaseConfigurer<O, B>>, List<BaseConfigurer<O, B>>> configurers;
    /**
     * 配置类添加初始化
     */
    private final List<BaseConfigurer<O, B>> configurersAddedInInitializing;
    /**
     * 共享的对象
     */
    private final Map<Class<?>, Object> sharedObjects;
    /**
     * 是否允许配置类有相同的类型
     */
    private final boolean allowConfigurersOfSameType;
    /**
     * 配置类加载状态
     */
    private AbstractConfiguredPlugInDriverBuilder.BuildState buildState;

    /**
     * 默认不允许配置类有相同的类型
     */
    protected AbstractConfiguredPlugInDriverBuilder() {
        this(false);
    }

    /**
     * 可配置的允许配置类有相同的类型构造方法
     * @param allowConfigurersOfSameType 是否允许配置类有相同的类型
     */
    protected AbstractConfiguredPlugInDriverBuilder(boolean allowConfigurersOfSameType) {
        this.logger = LogFactory.getLog(this.getClass());
        this.configurers = new LinkedHashMap<>();
        this.configurersAddedInInitializing = new ArrayList<>();
        this.sharedObjects = new HashMap<>();
        this.allowConfigurersOfSameType = allowConfigurersOfSameType;
    }

    public <C extends AbstractPlugInDriverConfigurerAdapter<O, B>> C apply(C configurer) throws Exception {
        configurer.setPluginDriverBuilder((B) this);
        this.add(configurer);
        return configurer;
    }

    public <C> void setSharedObject(Class<C> sharedType, C object) {
        this.sharedObjects.put(sharedType, object);
    }

    public <C> C getSharedObject(Class<C> sharedType) {
        return (C) this.sharedObjects.get(sharedType);
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

    /**
     * 获取配置类(多个)
     * @param clazz 配置类
     * @param <C>  BaseConfigurer
     * @return 配置类(多个)
     */
    public <C extends BaseConfigurer<O, B>> List<C> getConfigurers(Class<C> clazz) {
        List<C> configs = (List)this.configurers.get(clazz);
        return configs == null ? new ArrayList() : new ArrayList(configs);
    }

    /**
     * 移除配置类(多个)
     * @param clazz 配置类
     * @param <C>  BaseConfigurer
     * @return 配置类(多个)
     */
    public <C extends BaseConfigurer<O, B>> List<C> removeConfigurers(Class<C> clazz) {
        List<C> configs = (List)this.configurers.remove(clazz);
        return configs == null ? new ArrayList() : new ArrayList(configs);
    }

    /**
     * 获取配置类
     * @param clazz 配置类
     * @param <C>  BaseConfigurer
     * @return 配置类
     */
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

    /**
     * 移除配置类
     * @param clazz 配置类
     * @param <C>  BaseConfigurer
     * @return 配置类
     */
    public <C extends BaseConfigurer<O, B>> C removeConfigurer(Class<C> clazz) {
        List<BaseConfigurer<O, B>> configs = this.configurers.remove(clazz);
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
     * @return 获取或构建配置类
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
     * 判断configurers是否未创建
     * @return 是否未构建
     */
    private boolean isUnbuilt() {
        synchronized(this.configurers) {
            return this.buildState == AbstractConfiguredPlugInDriverBuilder.BuildState.UNBUILT;
        }
    }

    /**
     * 初始化之前
     * @throws Exception 异常
     */
    protected void beforeInit() throws Exception {
    }

    /**
     * 配置之前
     * @throws Exception 异常
     */
    protected void beforeConfigure() throws Exception {
    }

    /**
     * 执行创建
     * @return 配置类
     * @throws Exception 异常
     */
    protected abstract O performBuild() throws Exception;

    /**
     * 初始化
     * @throws Exception 异常
     */
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

    /**
     * 配置
     * @throws Exception 异常
     */
    private void configure() throws Exception {
        Collection<BaseConfigurer<O, B>> configurers = this.getConfigurers();

        for (BaseConfigurer<O, B> obBaseConfigurer : configurers) {
            obBaseConfigurer.configure((B) this);
        }

    }

    /**
     * 获取配置类集合
     * @return 配置类集合
     */
    private Collection<BaseConfigurer<O, B>> getConfigurers() {
        List<BaseConfigurer<O, B>> result = new ArrayList<>();

        for (List<BaseConfigurer<O, B>> baseConfigurers : this.configurers.values()) {
            result.addAll(baseConfigurers);
        }

        return result;
    }


    /**
     * 执行创建过程
     * @return 配置类
     * @throws Exception 异常
     */
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

    /**
     * 构建状态
     */
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

        BuildState(int order) {
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
