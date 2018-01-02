package com.alesharik.twitch.api.auth;

import com.alesharik.twitch.api.TwitchException;

public final class AuthCallbackServerException extends TwitchException {
    public AuthCallbackServerException(Throwable cause) {
        super(cause);
    }
}
