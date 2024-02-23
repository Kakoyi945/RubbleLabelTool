package com.label.rubblelabeltool.util.ex;

public class FileInfoErrorException extends UtilException{
    public FileInfoErrorException() {
        super();
    }

    public FileInfoErrorException(String message) {
        super(message);
    }

    public FileInfoErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileInfoErrorException(Throwable cause) {
        super(cause);
    }

    protected FileInfoErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
