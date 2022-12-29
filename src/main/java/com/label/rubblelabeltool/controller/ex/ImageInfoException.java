package com.label.rubblelabeltool.controller.ex;

public class ImageInfoException extends ControllerException{
    public ImageInfoException() {
        super();
    }

    public ImageInfoException(String message) {
        super(message);
    }

    public ImageInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageInfoException(Throwable cause) {
        super(cause);
    }

    protected ImageInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
