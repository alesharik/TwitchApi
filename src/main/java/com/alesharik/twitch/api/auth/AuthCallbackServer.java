package com.alesharik.twitch.api.auth;

import lombok.Getter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

final class AuthCallbackServer {
    private final URL callback;
    private final URL success;
    private final URL failure;
    private final int port;

    private ServerSocket serverSocket;
    @Getter
    private AuthCallbackRequestHttpHandler.Response response;

    public AuthCallbackServer(int port) {
        this(AuthCallbackServer.class.getClassLoader().getResource("com/alesharik/twitch/api/auth.html"),
                AuthCallbackServer.class.getClassLoader().getResource("com/alesharik/twitch/api/auth-success.html"),
                AuthCallbackServer.class.getClassLoader().getResource("com/alesharik/twitch/api/auth-failure.html"),
                port);
    }

    public AuthCallbackServer(URL callback, URL success, URL failure, int port) {
        this.callback = callback;
        this.success = success;
        this.failure = failure;
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            throw new AuthCallbackServerException(e);
        }
    }

    public void waitForResponse() {
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                AuthCallbackRequestHttpHandler httpHandler = new AuthCallbackRequestHttpHandler(callback, success, failure, socket);
                response = httpHandler.process();
                if(!(response instanceof AuthCallbackRequestHttpHandler.EmptyResponse))
                    return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
