package com.alesharik.twitch.api.helix;

import com.alesharik.twitch.api.TwitchException;
import lombok.Getter;

public final class TooManyRequestsException extends TwitchException {
    @Getter
    private final RateLimit rateLimit;

    public TooManyRequestsException(RateLimit rateLimit) {
        super("Too many requests!");
        this.rateLimit = rateLimit;
    }
}
