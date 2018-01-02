package com.alesharik.twitch.api.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Scope {
    EDIT_CLIPS("clips:edit"),
    EDIT_USER("user:edit"),
    READ_USER_EMAIL("user:read:email");

    @Getter
    private final String name;
}
