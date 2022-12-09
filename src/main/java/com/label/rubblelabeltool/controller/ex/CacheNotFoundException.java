package com.label.rubblelabeltool.controller.ex;

public class CacheNotFoundException extends RuntimeException{
    public CacheNotFoundException() {
        super();
    }

    public CacheNotFoundException(String message) {
        super(message);
    }

    public CacheNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheNotFoundException(Throwable cause) {
        super(cause);
    }

    protected CacheNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
