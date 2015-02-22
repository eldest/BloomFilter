package com.eldest.bllomfilter.hash;

public class HashException extends RuntimeException {

    public HashException() {
    }

    public HashException(String message) {
        super(message);
    }

    public HashException(String message, Throwable cause) {
        super(message, cause);
    }

    public HashException(Throwable cause) {
        super(cause);
    }

    public HashException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
