package com.label.rubblelabeltool.controller.ex;

public class FileOutOfSizeException extends FileUploadException{
    public FileOutOfSizeException() {
        super();
    }

    public FileOutOfSizeException(String message) {
        super(message);
    }

    public FileOutOfSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileOutOfSizeException(Throwable cause) {
        super(cause);
    }

    protected FileOutOfSizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
