package com.alesharik.twitch.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Auth {
    @Nonnull
    String getClientId();

    @Nullable
    String getToken();
}
