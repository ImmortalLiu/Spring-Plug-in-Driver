package com.fiicloud.plugindriver.core.builder;

/**
 * @author Anthony
 */
public interface BaseBuilder<O> {
    O build() throws Exception;
}
