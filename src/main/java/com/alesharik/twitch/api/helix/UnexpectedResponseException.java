package com.alesharik.twitch.api.helix;

import com.alesharik.twitch.api.TwitchException;

public final class UnexpectedResponseException extends TwitchException {
    public UnexpectedResponseException(String message) {
        super(message);
    }
}
