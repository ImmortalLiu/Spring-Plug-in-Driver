package com.fiicloud.plugindriver.core.entity;

import com.fiicloud.plugindriver.core.base.BaseBuilder;
import com.fiicloud.plugindriver.core.context.PlugInDriverContext;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Plugin.PluginBuilder and() {
        PlugInDriverContext.addPlugin(this);
        return Plugin.builder();
    }

    public void end() {
        PlugInDriverContext.addPlugin(this);
    }

    public static Plugin.PluginBuilder builder() {
        return new Plugin.PluginBuilder();
    }

    public static class PluginBuilder {
        private String id;
        private String name;
        private String url;
        private String bootClass;
        private List<String> scanPath;
        private List<Class> scanAnnotation;
        private String config;

        public PluginBuilder id(String id){
            this.id = id;
            return this;
        }

        public PluginBuilder name(String name){
            this.name = name;
            return this;
        }

        public PluginBuilder url(String url) {
            this.url = url;
            return this;
        }

        public PluginBuilder bootClass(String bootClass) {
            this.bootClass = bootClass;
            return this;
        }

        public PluginBuilder scanPath(String scanPath) {
            this.scanPath = Arrays.asList(scanPath.split(","));
            return this;
        }

        public PluginBuilder scanAnnotation(String scanAnnotation) {
            String[] classPaths = scanAnnotation.split(",");
            List<Class> classes = new ArrayList<>();
            try {
                for (String classPath : classPaths) {
                    Class c = Class.forName(classPath);
                    classes.add(c);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.scanAnnotation = classes;
            return this;
        }

        public PluginBuilder config(String config) {
            this.config = config;
            return this;
        }

        public Plugin build() {
            return new Plugin(this.id, this.name, this.url, this.bootClass, this.scanPath, this.scanAnnotation, this.config);
        }
    }
}
