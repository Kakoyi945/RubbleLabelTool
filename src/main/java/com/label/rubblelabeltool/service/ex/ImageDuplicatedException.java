package com.label.rubblelabeltool.service.ex;

/**
 * 数据在插入过程中产生的异常
 * 原因：服务器宕机，服务器宕机等
 */
public class ImageDuplicatedException extends ServiceException{
    public ImageDuplicatedException() {
        super();
    }

    public ImageDuplicatedException(String message) {
        super(message);
    }

    public ImageDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageDuplicatedException(Throwable cause) {
        super(cause);
    }

    protected ImageDuplicatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
