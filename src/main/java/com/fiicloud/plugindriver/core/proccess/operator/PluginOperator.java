package com.fiicloud.plugindriver.core.proccess.operator;

import com.fiicloud.plugindriver.core.entity.Plugin;
import com.fiicloud.plugindriver.core.exception.AlreadyDeleteException;
import com.fiicloud.plugindriver.core.exception.PluginNotFoundException;

import java.util.List;

/**
 * @author Anthony
 */
public interface PluginOperator {

    boolean upload(Plugin plugin) throws PluginNotFoundException;

    boolean delete(String id) throws PluginNotFoundException, AlreadyDeleteException;

    boolean reload(Plugin plugin) throws PluginNotFoundException;

    boolean reload(String ymlPath) throws PluginNotFoundException;

    boolean install(String id) throws PluginNotFoundException;

    boolean uninstall(String id) throws PluginNotFoundException;

    boolean enable(String id) throws PluginNotFoundException;

    boolean disabled(String id) throws PluginNotFoundException;

    List<Plugin> getAll();

    Plugin getById(String id);

    Plugin getByName(String name);

}
