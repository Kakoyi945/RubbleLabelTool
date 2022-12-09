package com.label.rubblelabeltool.controller.ex;

public class ParameterErrorException extends RuntimeException{
    public ParameterErrorException() {
        super();
    }

    public ParameterErrorException(String message) {
        super(message);
    }

    public ParameterErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterErrorException(Throwable cause) {
        super(cause);
    }

    protected ParameterErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
