package com.alesharik.twitch.api.exception;

/**
 * This is base for all twitch api exceptions
 */
public abstract class TwitchException extends RuntimeException {
    public TwitchException() {
    }

    public TwitchException(String message) {
        super(message);
    }

    public TwitchException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwitchException(Throwable cause) {
        super(cause);
    }

    protected TwitchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
