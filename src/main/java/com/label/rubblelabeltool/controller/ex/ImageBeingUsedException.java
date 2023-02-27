package com.label.rubblelabeltool.controller.ex;

public class ImageBeingUsedException extends ControllerException{
    public ImageBeingUsedException() {
        super();
    }

    public ImageBeingUsedException(String message) {
        super(message);
    }

    public ImageBeingUsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageBeingUsedException(Throwable cause) {
        super(cause);
    }

    protected ImageBeingUsedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
