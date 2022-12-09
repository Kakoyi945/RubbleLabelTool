package com.label.rubblelabeltool.util.ex;

public class MatEmptyException extends UtilException{
    public MatEmptyException() {
        super();
    }

    public MatEmptyException(String message) {
        super(message);
    }

    public MatEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatEmptyException(Throwable cause) {
        super(cause);
    }

    protected MatEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
