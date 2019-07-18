package com.fiicloud.plugindriver.core.configurer;

import com.fiicloud.plugindriver.core.builder.PlugInDriverBuilder;
import com.fiicloud.plugindriver.core.context.PlugInDriverContext;

/**
 * @author Anthony
 */
public abstract class AbstractPlugInDriverConfigurer<T extends AbstractPlugInDriverConfigurer<T, B>, B extends PlugInDriverBuilder<B>> extends AbstractPlugInDriverConfigurerAdapter<PlugInDriverContext, B> {

    public AbstractPlugInDriverConfigurer() {
    }

    /**
     * 取消配置类
     * @return PlugInDriverBuilder
     */
    public B disable() {
        ((PlugInDriverBuilder)this.getPluginDriverBuilder()).removeConfigurer(this.getClass());
        return this.getPluginDriverBuilder();
    }

}
