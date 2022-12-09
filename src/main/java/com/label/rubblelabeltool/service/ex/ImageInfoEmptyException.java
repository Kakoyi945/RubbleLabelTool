package com.label.rubblelabeltool.service.ex;

public class ImageInfoEmptyException extends ServiceException{
    public ImageInfoEmptyException() {
        super();
    }

    public ImageInfoEmptyException(String message) {
        super(message);
    }

    public ImageInfoEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageInfoEmptyException(Throwable cause) {
        super(cause);
    }

    protected ImageInfoEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
