/*
 *    Copyright 2017 alesharik
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
