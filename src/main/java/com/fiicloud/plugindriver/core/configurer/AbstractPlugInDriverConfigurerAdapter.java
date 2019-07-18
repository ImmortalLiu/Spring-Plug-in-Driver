package com.fiicloud.plugindriver.core.configurer;

import com.fiicloud.plugindriver.core.base.BaseBuilder;
import com.fiicloud.plugindriver.core.base.BaseConfigurer;

/**
 * @author Anthony
 */
public abstract class AbstractPlugInDriverConfigurerAdapter<O, B extends BaseBuilder<O>> implements BaseConfigurer<O, B> {
    private B pluginDriverBuilder;

    public AbstractPlugInDriverConfigurerAdapter() {
    }

    /**
     * 初始化
     * @param builder
     * @throws Exception
     */
    @Override
    public void init(B builder) throws Exception {

    }

    /**
     * 配置
     * @param builder
     * @throws Exception
     */
    @Override
    public void configure(B builder) throws Exception {

    }

    /**
     * 返回当前PlugInDriver
     * @return 当前PlugInDriver
     */
    public B and() {
        return this.getPluginDriverBuilder();
    }

    /**
     * 获取当前PlugInDriver
     * @return PlugInDriver
     */
    protected final B getPluginDriverBuilder() {
        if (this.pluginDriverBuilder == null) {
            throw new IllegalStateException("pluginBuilder cannot be null");
        } else {
            return this.pluginDriverBuilder;
        }
    }

    /**
     * 设置当前PlugInDriver
     * @param builder 当前PlugInDriver
     */
    public void setPluginDriverBuilder(B builder) {
        this.pluginDriverBuilder = builder;
    }
}
