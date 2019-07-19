package com.fiicloud.plugindriver.core.configurer;

import com.fiicloud.plugindriver.core.entity.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Anthony
 */
public class PluginInfoConfigurer<P extends AbstractPlugInDriverConfigurer> {
    private P plugInDriverConfigurer;

    public PluginInfoConfigurer() {}

    public P and() {
        return this.plugInDriverConfigurer;
    }

    public void setPlugInDriverConfigurer(P plugInDriverConfigurer) {
        this.plugInDriverConfigurer = plugInDriverConfigurer;
    }

    public PluginBuilder builder() {
        return new PluginBuilder();
    }

    public class PluginBuilder {
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

        public PluginInfoConfigurer with() {
            ((com.fiicloud.plugindriver.core.builder.PluginBuilder)PluginInfoConfigurer.this.plugInDriverConfigurer.getPluginDriverBuilder())
                .addPlugin(new Plugin(this.id, this.name, this.url, this.bootClass, this.scanPath, this.scanAnnotation, this.config));
            return PluginInfoConfigurer.this;
        }

        public JavaPlugInDriverConfigurer backJava() {
            ((com.fiicloud.plugindriver.core.builder.PluginBuilder)PluginInfoConfigurer.this.plugInDriverConfigurer.getPluginDriverBuilder())
                    .addPlugin(new Plugin(this.id, this.name, this.url, this.bootClass, this.scanPath, this.scanAnnotation, this.config));
            return (JavaPlugInDriverConfigurer)PluginInfoConfigurer.this.and();
        }

        public YmlPlugInDriverConfigurer backYml() {
            ((com.fiicloud.plugindriver.core.builder.PluginBuilder)PluginInfoConfigurer.this.plugInDriverConfigurer.getPluginDriverBuilder())
                    .addPlugin(new Plugin(this.id, this.name, this.url, this.bootClass, this.scanPath, this.scanAnnotation, this.config));
            return (YmlPlugInDriverConfigurer)PluginInfoConfigurer.this.and();
        }

        public JDBCPlugInDriverConfigurer backJdbc() {
            ((com.fiicloud.plugindriver.core.builder.PluginBuilder)PluginInfoConfigurer.this.plugInDriverConfigurer.getPluginDriverBuilder())
                    .addPlugin(new Plugin(this.id, this.name, this.url, this.bootClass, this.scanPath, this.scanAnnotation, this.config));
            return (JDBCPlugInDriverConfigurer)PluginInfoConfigurer.this.and();
        }
    }
}
