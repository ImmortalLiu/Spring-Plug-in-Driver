package com.fiicloud.plugindriver.core.builder;

import com.fiicloud.plugindriver.core.base.BaseBuilder;
import com.fiicloud.plugindriver.core.context.PlugInDriverContext;

public final class PlugInDriver extends AbstractConfiguredPlugInDriverBuilder<PlugInDriverContext, PlugInDriver> implements BaseBuilder<PlugInDriverContext>, PlugInDriverBuilder<PlugInDriver> {
    protected PlugInDriver(boolean allowConfigurersOfSameType) {
        super(allowConfigurersOfSameType);
    }

    @Override
    protected PlugInDriverContext performBuild() throws Exception {
        return null;
    }
}
