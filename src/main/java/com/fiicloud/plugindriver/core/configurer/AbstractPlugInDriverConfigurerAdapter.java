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

    @Override
    public void init(B builder) throws Exception {

    }

    @Override
    public void configure(B builder) throws Exception {

    }

    public B and() {
        return this.getPluginDriverBuilder();
    }

    protected final B getPluginDriverBuilder() {
        if (this.pluginDriverBuilder == null) {
            throw new IllegalStateException("pluginBuilder cannot be null");
        } else {
            return this.pluginDriverBuilder;
        }
    }

    public void setPluginDriverBuilder(B builder) {
        this.pluginDriverBuilder = builder;
    }
}
