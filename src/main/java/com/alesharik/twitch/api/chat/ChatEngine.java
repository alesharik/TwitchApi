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

package com.alesharik.twitch.api.chat;

import lombok.Getter;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface ChatEngine {
    Channel newChannel(Type type, String nick);

    void shutdownAll();

    @Getter
    enum Type {
        SECURE(443, lookup("irc.chat.twitch.tv")),
        INSECURE(6667, lookup("irc.chat.twitch.tv"));

        private final int port;
        private final InetAddress host;

        Type(int port, InetAddress host) {
            this.port = port;
            this.host = host;
        }

        private static InetAddress lookup(String s) {
            try {
                return InetAddress.getByName(s);
            } catch (UnknownHostException e) {
                throw new Error(e);
            }
        }
    }
}
