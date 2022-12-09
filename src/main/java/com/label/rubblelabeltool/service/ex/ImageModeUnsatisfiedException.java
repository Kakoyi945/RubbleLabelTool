package com.label.rubblelabeltool.service.ex;

public class ImageModeUnsatisfiedException extends ServiceException{
    public ImageModeUnsatisfiedException() {
        super();
    }

    public ImageModeUnsatisfiedException(String message) {
        super(message);
    }

    public ImageModeUnsatisfiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageModeUnsatisfiedException(Throwable cause) {
        super(cause);
    }

    protected ImageModeUnsatisfiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
