package com.alesharik.twitch.api.auth;

import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.function.Consumer;

@UtilityClass
public final class Authenticator {
    /**
     * @param opener
     * @param clientId
     * @param port
     * @param redirect
     * @param scopes
     * @return
     * @throws AuthCallbackServerException
     * @throws TwitchAuthenticationException
     */
    public static String getAuthToken(Consumer<URI> opener, String clientId, int port, String redirect, Scope... scopes) {
        AuthCallbackServer server = new AuthCallbackServer(port);
        return getToken(opener, clientId, server, redirect, scopes);
    }

    private static String getToken(Consumer<URI> opener, String clientId, AuthCallbackServer server, String redirect, Scope... scopes) {
        StringBuilder scope = new StringBuilder();
        for(int i = 0; i < scopes.length; i++) {
            Scope scope1 = scopes[i];
            scope.append(scope1.getName());
            if(i + 1 < scopes.length)
                scope.append(',');
        }
        String state = RandomStringUtils.random(64);
        URI url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.twitch.tv")
                .addPathSegment("kraken")
                .addPathSegment("oauth2")
                .addPathSegment("authorize")
                .addQueryParameter("client_id", clientId)
                .addQueryParameter("redirect_uri", redirect)
                .addQueryParameter("response_type", "token")
                .addQueryParameter("scope", scope.toString())
                .addQueryParameter("state", state)
                .build()
                .uri();
        server.start();
        opener.accept(url);
        server.waitForResponse();
        AuthCallbackRequestHttpHandler.Response response = server.getResponse();
        if(response.isError())
            throw new TwitchAuthenticationException(((AuthCallbackRequestHttpHandler.ErrorResponse) response).getError(), ((AuthCallbackRequestHttpHandler.ErrorResponse) response).getDescription(), Arrays.asList(scopes));
        else if(response.isSuccess())
            return ((AuthCallbackRequestHttpHandler.SuccessResponse) response).getToken();
        else
            throw new TwitchAuthenticationException("NO RESPONSE", "NO RESPONSE", Arrays.asList(scopes));
    }

    /**
     * @param opener
     * @param clientId
     * @param auth
     * @param success
     * @param failure
     * @param port
     * @param redirect
     * @param scopes
     * @return
     * @throws AuthCallbackServerException
     * @throws TwitchAuthenticationException
     */
    public static String getAuthToken(Consumer<URI> opener, String clientId, URL auth, URL success, URL failure, int port, String redirect, Scope... scopes) {
        AuthCallbackServer server = new AuthCallbackServer(auth, success, failure, port);
        return getToken(opener, clientId, server, redirect, scopes);
    }
}
