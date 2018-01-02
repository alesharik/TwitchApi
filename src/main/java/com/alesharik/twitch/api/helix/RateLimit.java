package com.alesharik.twitch.api.helix;

import lombok.Getter;

@Getter
public final class RateLimit {//TODO custom ratelimits
    private volatile int limit = 30;
    private volatile int remaining = 30;
    private volatile long reset = System.currentTimeMillis();

    public boolean canSend() {
        long current = System.currentTimeMillis();
        return reset <= current || remaining > 0;
    }

    void update(int limit, int remaining, long reset) {
        this.limit = limit;
        this.remaining = remaining;
        this.reset = reset;
    }
}
