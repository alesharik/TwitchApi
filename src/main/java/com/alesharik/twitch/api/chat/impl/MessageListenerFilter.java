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

package com.alesharik.twitch.api.chat.impl;

import com.alesharik.twitch.api.chat.Channel;
import com.alesharik.twitch.api.chat.Message;
import com.alesharik.twitch.api.chat.MessageListener;
import com.alesharik.twitch.api.chat.irc.IRCChannel;
import com.alesharik.twitch.api.chat.irc.IRCMessage;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
final class MessageListenerFilter implements IRCChannel.Listener {
    private final ExecutorService executorService;
    private final List<MessageListener> listeners;
    private final Channel channel;

    @Override
    public void listen(IRCMessage message) {
        if(message instanceof Message)
            executorService.execute(new ListenTask(listeners, (Message) message, channel));
    }

    @RequiredArgsConstructor
    private static final class ListenTask implements Runnable {
        private final List<MessageListener> listeners;
        private final Message message;
        private final Channel channel;

        @Override
        public void run() {
            for(MessageListener listener : listeners) {
                try {
                    listener.listen(message, channel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
