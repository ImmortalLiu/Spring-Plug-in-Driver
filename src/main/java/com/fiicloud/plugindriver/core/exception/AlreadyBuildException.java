package com.fiicloud.plugindriver.core.exception;

/**
 * @author Anthony
 */
public class AlreadyBuildException extends IllegalStateException {
    public AlreadyBuildException(String message) {
        super(message);
    }
}
