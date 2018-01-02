package com.alesharik.twitch.api;

import com.alesharik.twitch.api.auth.Authenticator;
import com.alesharik.twitch.api.auth.Scope;
import com.alesharik.twitch.api.helix.async.Helix;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class Twitch {
    protected final String clientId;
    protected final OkHttpClient client;

    @Setter
    @Getter
    protected String token;

    public Twitch(String clientId) {
        this.clientId = clientId;
        this.client = new OkHttpClient();
    }

    /**
     * Fill token from client. Use only on desktop version
     *
     * @param callback default is <code>http://127.0.0.1:23522/authorize.html</code>
     */
    public void authorizeClient(URI callback, int port, Scope... scopes) {
        if(!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
            throw new IllegalStateException("Supports only desktop systems with browsers!");
        if(scopes.length == 0)
            return;
        token = Authenticator.getAuthToken(url -> {
            try {
                Desktop.getDesktop().browse(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, clientId, port, callback.toASCIIString(), scopes);
    }

    public Helix getHelix() {
        return new Helix(client, new AuthImpl());
    }

    private final class AuthImpl implements Auth {
        @Nonnull
        @Override
        public String getClientId() {
            return clientId;
        }

        @Nullable
        @Override
        public String getToken() {
            return token;
        }
    }
}
