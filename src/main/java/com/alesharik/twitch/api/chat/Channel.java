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

import com.alesharik.twitch.api.Twitch;
import com.alesharik.twitch.api.chat.irc.IRCChannel;

public interface Channel {
    IRCChannel getIRCChannel();

    void registerMessageListener(MessageListener messageListener);

    void unregisterMessageListener(MessageListener messageListener);

    void send(Message message);

    Twitch getTwitch();

    void connect(String user);

    void shutdown();
}
