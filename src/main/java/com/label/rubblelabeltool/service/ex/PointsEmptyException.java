package com.label.rubblelabeltool.service.ex;

public class PointsEmptyException extends ServiceException{
    public PointsEmptyException() {
        super();
    }

    public PointsEmptyException(String message) {
        super(message);
    }

    public PointsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PointsEmptyException(Throwable cause) {
        super(cause);
    }

    protected PointsEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
