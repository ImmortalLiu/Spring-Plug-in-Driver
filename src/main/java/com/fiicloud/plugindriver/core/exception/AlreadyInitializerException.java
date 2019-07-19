package com.fiicloud.plugindriver.core.exception;

/**
 * @author Anthony
 */
public class AlreadyInitializerException extends IllegalStateException {
    public AlreadyInitializerException(String message) {
        super(message);
    }
}
