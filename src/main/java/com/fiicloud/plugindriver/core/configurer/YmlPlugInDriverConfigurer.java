package com.fiicloud.plugindriver.core.configurer;

import com.fiicloud.plugindriver.core.builder.PlugInDriverBuilder;

/**
 * @author Anthony
 */
public class YmlPlugInDriverConfigurer<H extends PlugInDriverBuilder<H>> extends AbstractPlugInDriverConfigurer<YmlPlugInDriverConfigurer<H>, H> {

    /**
     * yml路径
     */
    private String ymlPath;

    public YmlPlugInDriverConfigurer() {
    }
}
