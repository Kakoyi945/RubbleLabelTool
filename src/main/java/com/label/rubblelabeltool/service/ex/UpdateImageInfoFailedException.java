package com.label.rubblelabeltool.service.ex;

public class UpdateImageInfoFailedException extends ServiceException{
    public UpdateImageInfoFailedException() {
        super();
    }

    public UpdateImageInfoFailedException(String message) {
        super(message);
    }

    public UpdateImageInfoFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateImageInfoFailedException(Throwable cause) {
        super(cause);
    }

    protected UpdateImageInfoFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
