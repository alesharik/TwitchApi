package com.alesharik.twitch.api;

public abstract class TwitchException extends RuntimeException{
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
}
