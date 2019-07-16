package com.fiicloud.plugindriver.core.builder;

import com.fiicloud.plugindriver.core.base.BaseBuilder;
import com.fiicloud.plugindriver.core.configurer.AbstractPlugInDriverConfigurerAdapter;
import com.fiicloud.plugindriver.core.configurer.JDBCPlugInDriverConfigurer;
import com.fiicloud.plugindriver.core.configurer.JavaPlugInDriverConfigurer;
import com.fiicloud.plugindriver.core.configurer.YmlPlugInDriverConfigurer;
import com.fiicloud.plugindriver.core.context.PlugInDriverContext;
import com.fiicloud.plugindriver.core.entity.Plugin;
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

    private <C extends AbstractPlugInDriverConfigurerAdapter<PlugInDriverContext, PlugInDriver>> C getOrApply(C configurer) throws Exception {
        C existingConfig = (C) this.getConfigurer(configurer.getClass());
        return existingConfig != null ? existingConfig : this.apply(configurer);
    }

    public JavaPlugInDriverConfigurer<PlugInDriver> java() throws Exception{
        return this.getOrApply(new JavaPlugInDriverConfigurer<>());
    }

    public JDBCPlugInDriverConfigurer<PlugInDriver> jdbc() throws Exception{
        return this.getOrApply(new JDBCPlugInDriverConfigurer<>());
    }

    public YmlPlugInDriverConfigurer<PlugInDriver> yml() throws Exception{
        return this.getOrApply(new YmlPlugInDriverConfigurer<>());
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


    protected PlugInDriver(boolean allowConfigurersOfSameType) {
        super(allowConfigurersOfSameType);
    }

    @Override
    protected PlugInDriverContext performBuild() throws Exception {
        return null;
    }

    @Override
    public <C> void setSharedObject(Class<C> sharedType, C object) {
        super.setSharedObject(sharedType, object);
    }
}
