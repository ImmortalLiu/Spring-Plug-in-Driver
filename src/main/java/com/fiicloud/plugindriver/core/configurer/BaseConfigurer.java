package com.fiicloud.plugindriver.core.configurer;

import com.fiicloud.plugindriver.core.builder.BaseBuilder;

/**
 * @author Anthony
 */
public interface BaseConfigurer<O, B extends BaseBuilder<O>> {

    void init(B var1) throws Exception;

    void configure(B var1) throws Exception;

}
