package com.fiicloud.plugindriver.core.exception;

/**
 * @author Anthony
 */
public class AlreadyInstallException extends IllegalStateException {
    public AlreadyInstallException(String message) {
        super(message);
    }
}
