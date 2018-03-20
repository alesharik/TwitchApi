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

package com.alesharik.twitch.api;

import com.alesharik.twitch.api.auth.Scope;
import com.alesharik.twitch.api.chat.Channel;
import com.alesharik.twitch.api.chat.ChatEngine;
import com.alesharik.twitch.api.chat.impl.ChatEngineImpl;
import com.alesharik.twitch.api.chat.irc.impl.IRCChannelFactoryImpl;
import com.alesharik.twitch.api.chat.message.TextMessage;
import org.junit.Test;

import java.net.URI;
import java.util.concurrent.ForkJoinPool;

public class TwitchTest {
    @Test
    public void authTest() throws Exception {
        Twitch twitch = new Twitch("ub00uwkym079kr09g8h8oac426ifns");
        twitch.authorizeClient(new URI("http://127.0.0.1:23522/authorize.html"), 23522, Scope.CHAT);

        ChatEngine engine = new ChatEngineImpl(twitch, ForkJoinPool.commonPool(), new IRCChannelFactoryImpl());
        Channel channel = engine.newChannel(ChatEngine.Type.SECURE, "alexchannell");
        channel.registerMessageListener((message, channel1) -> System.out.println(message.toIRCString()));
        channel.connect("alexchannell");
        channel.send(new TextMessage("alexchannell", "test"));

        Thread.sleep(100000000);
    }
}