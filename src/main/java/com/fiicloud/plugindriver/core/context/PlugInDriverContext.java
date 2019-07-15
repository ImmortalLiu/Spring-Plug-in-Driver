package com.fiicloud.plugindriver.core.context;

import com.fiicloud.plugindriver.core.entity.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anthony
 */
public class PlugInDriverContext {

    /**
     * 存储所有需要扫描的注解或类
     */
    private static List<Class> scanAnnotations = new ArrayList<>();

    /**
     * 存储所有的插件
     */
    private static List<Plugin> plugins = new ArrayList<>();

    /**
     * 添加插件
     * @param plugin 插件
     */
    public static void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    /**
     * 添加需要扫描的注解或类
     * @param c 需要扫描的注解或类
     */
    public static void addScanAnnotation(Class c) {
        scanAnnotations.add(c);
    }
}
