package com.fiicloud.plugindriver.core.builder;

import com.fiicloud.plugindriver.core.base.BaseBuilder;
import com.fiicloud.plugindriver.core.configurer.AbstractPlugInDriverConfigurerAdapter;
import com.fiicloud.plugindriver.core.configurer.JDBCPlugInDriverConfigurer;
import com.fiicloud.plugindriver.core.configurer.JavaPlugInDriverConfigurer;
import com.fiicloud.plugindriver.core.configurer.YmlPlugInDriverConfigurer;
import com.fiicloud.plugindriver.core.context.PlugInDriverContext;
import com.fiicloud.plugindriver.core.entity.Plugin;
import com.fiicloud.plugindriver.core.exception.ConfigurerException;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Anthony
 */
@SuppressWarnings("unchecked")
public final class PlugInDriver extends AbstractConfiguredPlugInDriverBuilder<PlugInDriverContext, PlugInDriver> implements BaseBuilder<PlugInDriverContext>, PlugInDriverBuilder<PlugInDriver> {
    /**
     * 存储所有需要扫描的注解或类
     */
    private List<Class> scanAnnotations = new ArrayList<>();
    /**
     * 存储所有的插件
     */
    private List<Plugin> plugins = new ArrayList<>();
    /**
     * 插件驱动使用哪种配置方式
     */
    private PlugInDriver.ConfigurerMode mode;

    /**
     * @param sharedObjects 共享的对象
     */
    public PlugInDriver(Map<Class<?>, Object> sharedObjects) {
        for (Map.Entry<Class<?>, Object> classObjectEntry : sharedObjects.entrySet()) {
            this.setSharedObject((Class) classObjectEntry.getKey(), classObjectEntry.getValue());
        }
    }

    /**
     * 获取spring容器上下文
     * @return spring容器上下文
     */
    private ApplicationContext getContext() {
        return this.getSharedObject(ApplicationContext.class);
    }

    /**
     * 获取或应用configurer
     * @param configurer 配置类
     * @param <C> 配置类
     * @return 配置类
     * @throws Exception 异常
     */
    private <C extends AbstractPlugInDriverConfigurerAdapter<PlugInDriverContext, PlugInDriver>> C getOrApply(C configurer) throws Exception {
        C existingConfig = (C) this.getConfigurer(configurer.getClass());
        return existingConfig != null ? existingConfig : this.apply(configurer);
    }

    /**
     * 设置插件驱动配置方式
     * @param configurerMode 插件驱动配置方式 java/jdbc/yml
     * @return PlugInDriver
     */
    public PlugInDriver mode(PlugInDriver.ConfigurerMode configurerMode) {
        this.mode = configurerMode;
        return this;
    }

    /**
     * 使用Java 配置方式
     * @return Java配置方式类
     * @throws Exception 异常
     */
    public JavaPlugInDriverConfigurer<PlugInDriver> java() throws Exception{
        if (!mode.isJava()) {
            throw new ConfigurerException("mode is not java");
        }
        return this.getOrApply(new JavaPlugInDriverConfigurer<>());
    }

    /**
     * 使用jdbc 配置方式
     * @return Jdbc配置方式类
     * @throws Exception 异常
     */
    public JDBCPlugInDriverConfigurer<PlugInDriver> jdbc() throws Exception{
        if (!mode.isJdbc()) {
            throw new ConfigurerException("mode is not jdbc");
        }
        return this.getOrApply(new JDBCPlugInDriverConfigurer<>());
    }

    /**
     * 使用yml 配置方式
     * @return yml配置方式类
     * @throws Exception 异常
     */
    public YmlPlugInDriverConfigurer<PlugInDriver> yml() throws Exception{
        if (!mode.isYml()) {
            throw new ConfigurerException("mode is not yml");
        }
        return this.getOrApply(new YmlPlugInDriverConfigurer<>());
    }

    /**
     * 连接配置
     * @return PlugInDriver
     */
    public PlugInDriver and() {
        return PlugInDriver.this;
    }

    /**
     * 添加插件
     * @param plugin 插件
     */
    public void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    /**
     * 添加需要扫描的注解或类
     * @param c 需要扫描的注解或类
     */
    public void addScanAnnotation(Class c) {
        scanAnnotations.add(c);
    }

    /**
     * 执行创建
     * @return 配置类
     * @throws Exception 异常
     */
    @Override
    protected PlugInDriverContext performBuild() throws Exception {
        return new PlugInDriverContext();
    }

    /**
     * 设置共享的对象
     * @param sharedType 共享的对象类型
     * @param object 共享的对象
     * @param <C> 共享的对象
     */
    @Override
    public <C> void setSharedObject(Class<C> sharedType, C object) {
        super.setSharedObject(sharedType, object);
    }

    private static enum ConfigurerMode {
        /**
         * 使用Java方式
         */
        JAVA("JAVA"),
        /**
         * 使用jdbc方式
         */
        JDBC("JDBC"),
        /**
         * 使用yml方式
         */
        YML("YML");

        private final String mode;

        private ConfigurerMode(String mode) {
            this.mode = mode;
        }

        public boolean isJava() {
            return JAVA == this;
        }

        public boolean isJdbc() {
            return JDBC == this;
        }

        public boolean isYml() {
            return YML == this;
        }

        public String getMode() {
            return mode;
        }
    }
}
