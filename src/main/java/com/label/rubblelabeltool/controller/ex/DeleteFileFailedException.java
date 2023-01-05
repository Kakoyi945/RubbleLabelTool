package com.label.rubblelabeltool.controller.ex;

public class DeleteFileFailedException extends ControllerException{
    public DeleteFileFailedException() {
        super();
    }

    public DeleteFileFailedException(String message) {
        super(message);
    }

    public DeleteFileFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteFileFailedException(Throwable cause) {
        super(cause);
    }

    protected DeleteFileFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
