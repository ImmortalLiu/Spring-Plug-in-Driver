package com.fiicloud.plugindriver.core.exception;

/**
 * @author Anthony
 */
public class AlreadyDeleteException extends IllegalStateException {
    public AlreadyDeleteException(String message) {
        super(message);
    }
}
