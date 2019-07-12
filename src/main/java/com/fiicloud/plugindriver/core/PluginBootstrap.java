package com.fiicloud.plugindriver.core;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Anthony
 */
public interface PluginBootstrap {
    void boot(ConfigurableApplicationContext context);
}
