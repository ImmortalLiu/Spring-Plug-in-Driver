package com.fiicloud.plugindriver.core.builder;

import com.fiicloud.plugindriver.core.base.BaseBuilder;
import com.fiicloud.plugindriver.core.exception.AlreadyBuiltException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Anthony
 */
public abstract class AbstractPlugInDriverBuilder<O> implements BaseBuilder<O> {
    private AtomicBoolean building = new AtomicBoolean();
    private O object;

    @Override
    public final O build() throws Exception {
        if (this.building.compareAndSet(false, true)) {
            this.object = this.doBuild();
            return this.object;
        } else {
            throw new AlreadyBuiltException("This object has already been built");
        }
    }

    public final O getObject() {
        if (!this.building.get()) {
            throw new IllegalStateException("This object has not been built");
        } else {
            return this.object;
        }
    }

    protected abstract O doBuild() throws Exception;
}
