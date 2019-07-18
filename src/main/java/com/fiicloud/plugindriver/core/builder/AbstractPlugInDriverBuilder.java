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

    /**
     * 获取配置类对象
     * @return 配置类对象
     */
    public final O getObject() {
        if (!this.building.get()) {
            throw new IllegalStateException("This object has not been built");
        } else {
            return this.object;
        }
    }

    /**
     * 执行配置类构建
     * @return 配置类对象
     * @throws Exception 异常
     */
    protected abstract O doBuild() throws Exception;
}
