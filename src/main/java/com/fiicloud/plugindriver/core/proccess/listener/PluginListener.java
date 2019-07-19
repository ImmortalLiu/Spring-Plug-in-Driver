package com.fiicloud.plugindriver.core.proccess.listener;

import com.fiicloud.plugindriver.core.entity.State;

/**
 * @author Anthony
 */
public interface PluginListener {

    void before();

    void process();

    void complete();

    void failure();

    State now();

}
