package com.fiicloud.plugindriver.core.builder;

import com.fiicloud.plugindriver.core.configurer.BaseConfigurer;
import com.fiicloud.plugindriver.core.context.PlugInDriverContext;

public interface PlugInDriverBuilder<P extends PlugInDriverBuilder<P>> extends BaseBuilder<PlugInDriverContext> {

    <C extends BaseConfigurer<PlugInDriverContext, P>> C getConfigurer(Class<C> var1);

    <C extends BaseConfigurer<PlugInDriverContext, P>> C removeConfigurer(Class<C> var1);

}
