package com.fiicloud.plugindriver.core.configurer;

import com.fiicloud.plugindriver.core.builder.PlugInDriverBuilder;

/**
 * @author Anthony
 */
public class JavaPlugInDriverConfigurer<H extends PlugInDriverBuilder<H>> extends AbstractPlugInDriverConfigurer<JavaPlugInDriverConfigurer<H>, H> {

    public JavaPlugInDriverConfigurer() {
    }

    public PluginInfoConfigurer<JavaPlugInDriverConfigurer> loadOrder() {
        PluginInfoConfigurer<JavaPlugInDriverConfigurer> pluginInfoConfigurer = new PluginInfoConfigurer<>();
        pluginInfoConfigurer.setPlugInDriverConfigurer(this);
        return pluginInfoConfigurer;
    }
}
