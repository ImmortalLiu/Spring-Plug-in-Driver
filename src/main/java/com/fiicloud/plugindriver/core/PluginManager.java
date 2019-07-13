package com.fiicloud.plugindriver.core;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Anthony
 */
public class PluginManager {
    private String jarLibPath;
    private String relationYml;
    private URLClassLoader classLoader;

    private static List<Class> springIOCAnnotations = new ArrayList<>();

    private static List<PluginBootstrap> bootClasses = new ArrayList<>();

    private static List<Plugin> plugins = new ArrayList<>();

    // 可以放进spring容器的类的注解，待优化
    static {
        springIOCAnnotations.add(Controller.class);
        springIOCAnnotations.add(Service.class);
        springIOCAnnotations.add(Repository.class);
        springIOCAnnotations.add(Component.class);
    }

    public PluginManager jarLibPath(String path) {
        this.setJarLibPath(path);
        return this;
    }

    public PluginManager ymlPath(String path) {
        this.setRelationYml(path);
        return this;
    }

    public PluginManager annotations(Class... annotations) {
        for (Class annotation : annotations) {
            if (!springIOCAnnotations.contains(annotation)) {
                springIOCAnnotations.add(annotation);
            }
        }
        return this;
    }

    public Plugin loadOrder(){
        return new Plugin();
    }

    public PluginManager and() {
        return this;
    }

//    public void register(ConfigurableApplicationContext context) {
//        getPlugins().forEach(plugin -> {
//            addJarToClasspath(plugin);
//
//            JarFile jarFile = readJarFile(plugin);
//            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
//
//            fetchBootClass(plugin);
//
//            if (plugin.getScanPath() != null) {
//                //遍历jar包，将指定路径下的类添加到spring容器
//                traverseJar(jarEntryEnumeration, context, plugin);
//            }
//        });
//    }

    public void boot(ConfigurableApplicationContext context) {
        bootClasses.forEach(bootClass -> bootClass.boot(context));
    }

    private PluginManager loadPlugins() {
        return this;
    }

    private void fetchBootClass(Plugin plugin) {
        if (plugin.getBootClass() == null || plugin.getBootClass().trim().isEmpty()) {
            return;
        }
        try {
            PluginBootstrap pluginBootStrap = (PluginBootstrap) classLoader.loadClass(plugin.getBootClass()).newInstance();
            bootClasses.add(pluginBootStrap);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("获取插件boot函数失败，插件名称：" + plugin.getName());
        }
    }

    private void traverseJar(Enumeration<JarEntry> jarEntryEnumeration, ConfigurableApplicationContext context,
                             Plugin plugin) {
        while (jarEntryEnumeration.hasMoreElements()) {
            JarEntry jarEntry = jarEntryEnumeration.nextElement();
            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")
                    || plugin.getScanPath().stream().noneMatch(scanPath -> jarEntry.getName().startsWith(scanPath))) {
                continue;
            }

            String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
            className = className.replace('/', '.');

            try {
                Class c = classLoader.loadClass(className);
                BeanDefinitionRegistry definitionRegistry = (BeanDefinitionRegistry) context.getBeanFactory();
                for (Class springIocAnnotationClass : springIOCAnnotations) {
                    if (c.getAnnotation(springIocAnnotationClass) != null) {
                        definitionRegistry.registerBeanDefinition(className,
                                BeanDefinitionBuilder.genericBeanDefinition(c).getBeanDefinition());
                        break;
                    }
                }
            } catch (NoClassDefFoundError | ClassNotFoundException e1) {
                traverseJar(jarEntryEnumeration, context, plugin);
            }
        }
    }

    private void addJarToClasspath(Plugin plugin) {
        try {
            File file = new File(plugin.getUrl());
            URL url = file.toURI().toURL();

            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        } catch (IllegalAccessException | MalformedURLException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("添加插件jar包到classpath失败，插件名称：" + plugin.getName());
        }
    }

    private JarFile readJarFile(Plugin plugin) {
        try {
            return new JarFile(plugin.getUrl());
        } catch (IOException e) {
            throw new RuntimeException("获取插件中的文件信息失败，插件名称：" + plugin.getName());
        }
    }

    private void setJarLibPath(String jarLibPath) {
        this.jarLibPath = jarLibPath;
    }

    public String getJarLibPath() {
        return this.jarLibPath;
    }

    private void setRelationYml(String relationYml) {
        this.relationYml = relationYml;
    }

    public String getRelationYml() {
        return this.relationYml;
    }

    public void setClassLoader(URLClassLoader urlClassLoader) {
        this.classLoader = urlClassLoader;
    }

    private class Plugin {
        private String id;
        private String name;
        private String url;
        private String bootClass;
        private List<String> scanPath;
        private String config;

        Plugin(){}
        Plugin(String id, String name, String url, String bootClass, String scanPath, String config){
            this.setId(id);
            this.setName(name);
            this.setUrl(url);
            this.setBootClass(bootClass);
            this.setScanPath(scanPath);
            this.setConfig(config);
        }

        public Plugin id(String id){
            this.setId(id);
            return this;
        }

        public Plugin name(String name){
            this.setName(name);
            return this;
        }

        public Plugin url(String url) {
            this.setUrl(url);
            return this;
        }

        public Plugin bootClass(String bootClass) {
            this.setBootClass(bootClass);
            return this;
        }

        public Plugin scanPath(String scanPath) {
            this.setScanPath(scanPath);
            return this;
        }

        public Plugin config(String config) {
            this.setConfig(config);
            return this;
        }

        public Plugin and() {
            PluginManager.plugins.add(this);
            return new Plugin();
        }

        public PluginManager end() {
            return PluginManager.this;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBootClass() {
            return bootClass;
        }

        public void setBootClass(String bootClass) {
            this.bootClass = bootClass;
        }

        public List<String> getScanPath() {
            return scanPath;
        }

        public void setScanPath(String scanPath) {
            this.scanPath = Arrays.asList(scanPath.split(","));
        }

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }
    }
}
