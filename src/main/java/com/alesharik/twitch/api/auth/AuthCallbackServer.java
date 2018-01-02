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
