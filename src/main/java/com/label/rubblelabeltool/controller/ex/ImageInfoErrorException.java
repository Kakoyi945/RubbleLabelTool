package com.label.rubblelabeltool.controller.ex;

public class ImageInfoErrorException extends ImageInfoException{
    public ImageInfoErrorException() {
        super();
    }

    public ImageInfoErrorException(String message) {
        super(message);
    }

    public ImageInfoErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageInfoErrorException(Throwable cause) {
        super(cause);
    }

    protected ImageInfoErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
