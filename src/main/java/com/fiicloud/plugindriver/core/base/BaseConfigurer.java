package com.fiicloud.plugindriver.core.base;

/**
 * @author Anthony
 */
public interface BaseConfigurer<O, B extends BaseBuilder<O>> {

    void init(B var1) throws Exception;

    void configure(B var1) throws Exception;

}
