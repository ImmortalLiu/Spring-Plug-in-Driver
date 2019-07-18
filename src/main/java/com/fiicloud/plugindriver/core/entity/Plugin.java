package com.fiicloud.plugindriver.core.entity;

import java.util.List;

/**
 * @author Anthony
 */
public class Plugin {
    private String id;
    private String name;
    private String url;
    private String bootClass;
    private List<String> scanPath;
    private List<Class> scanAnnotation;
    private String config;

    public Plugin() {
    }

    public Plugin(String id, String name, String url, String bootClass, List<String> scanPath, List<Class> scanAnnotation, String config) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.bootClass = bootClass;
        this.scanPath = scanPath;
        this.scanAnnotation = scanAnnotation;
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getBootClass() {
        return bootClass;
    }

    public List<String> getScanPath() {
        return scanPath;
    }

    public List<Class> getScanAnnotation() {
        return scanAnnotation;
    }

    public String getConfig() {
        return config;
    }

}
