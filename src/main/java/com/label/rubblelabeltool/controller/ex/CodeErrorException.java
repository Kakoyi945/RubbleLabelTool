package com.label.rubblelabeltool.controller.ex;

public class CodeErrorException extends RuntimeException{
    public CodeErrorException() {
        super();
    }

    public CodeErrorException(String message) {
        super(message);
    }

    public CodeErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeErrorException(Throwable cause) {
        super(cause);
    }

    protected CodeErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
