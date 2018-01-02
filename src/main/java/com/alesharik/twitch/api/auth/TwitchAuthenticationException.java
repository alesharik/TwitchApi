package com.alesharik.twitch.api.auth;

import com.alesharik.twitch.api.TwitchException;
import lombok.Getter;

import java.util.List;

@Getter
public final class TwitchAuthenticationException extends TwitchException {
    private final String name;
    private final String description;
    private final List<Scope> scopes;

    public TwitchAuthenticationException(String name, String description, List<Scope> scopes) {
        super(name);
        this.name = name;
        this.description = description;
        this.scopes = scopes;
    }

    @Override
    public String getMessage() {
        return name + ": " + description;
    }
}
