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

import com.alesharik.twitch.api.Twitch;
import com.alesharik.twitch.api.chat.Channel;
import com.alesharik.twitch.api.chat.ChatEngine;
import com.alesharik.twitch.api.chat.Message;
import com.alesharik.twitch.api.chat.MessageListener;
import com.alesharik.twitch.api.chat.TwitchChatException;
import com.alesharik.twitch.api.chat.irc.IRCChannel;
import com.alesharik.twitch.api.chat.irc.IRCChannelFactory;
import com.alesharik.twitch.api.chat.message.JoinMessage;
import com.alesharik.twitch.api.chat.message.NickMessage;
import com.alesharik.twitch.api.chat.message.PasswordMessage;
import com.alesharik.twitch.api.chat.message.TextMessage;
import com.alesharik.twitch.api.chat.message.UnknownCommandMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public final class ChatEngineImpl implements ChatEngine {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final Twitch twitch;
    private final ExecutorService executorService;
    private final IRCChannelFactory ircChannelFactory;
    private final List<Channel> channels = new ArrayList<>();
    @Getter
    private final ThreadGroup ircThreadGroup = new ThreadGroup("IRC-Threads" + COUNTER.getAndIncrement());
    private final AtomicInteger ircThreadCounter = new AtomicInteger(0);

    @Override
    public Channel newChannel(Type type, String nick) {
        try {
            IRCChannel irc = ircChannelFactory.create(type.getHost(), type.getPort(), type == Type.SECURE, runnable -> {
                Thread thread = new Thread(ircThreadGroup, runnable, "IRCThread" + ircThreadCounter.getAndIncrement());
                thread.setDaemon(true);
                thread.start();
            }, executorService);
            AtomicReference<Channel> channelRef = new AtomicReference<>();
            ChannelImpl channel = new ChannelImpl(irc, twitch, executorService, () -> channels.remove(channelRef.get()));
            channelRef.set(channel);
            channels.add(channel);

            //Auth:
            channel.send(new PasswordMessage(twitch.getToken()));
            channel.send(new NickMessage(nick));

            return channel;
        } catch (IOException e) {
            throw new TwitchChatException(e);
        }
    }

    @Override
    public void shutdownAll() {
        for(Channel channel : channels)
            channel.shutdown();
    }

    private static final class ChannelImpl implements Channel {
        private final IRCChannel ircChannel;
        @Getter
        private final Twitch twitch;
        private final ExecutorService executorService;
        private final Runnable shutdownHook;
        private final List<MessageListener> messageListeners = new CopyOnWriteArrayList<>();

        public ChannelImpl(IRCChannel ircChannel, Twitch twitch, ExecutorService executorService, Runnable shutdownHook) {
            this.ircChannel = ircChannel;
            this.twitch = twitch;
            this.executorService = executorService;
            this.shutdownHook = shutdownHook;
            registerStandard();
        }

        @Override
        public IRCChannel getIRCChannel() {
            return ircChannel;
        }

        @Override
        public void registerMessageListener(MessageListener messageListener) {
            messageListeners.add(messageListener);
        }

        @Override
        public void unregisterMessageListener(MessageListener messageListener) {
            messageListeners.remove(messageListener);
        }

        @Override
        public void send(Message message) {
            ircChannel.sendMessage(message);
        }

        @Override
        public void connect(String user) {
            send(new JoinMessage(user));
        }

        @Override
        public void shutdown() {
            ircChannel.shutdown();
            shutdownHook.run();
        }

        private void registerStandard() {
            ircChannel.registerListener(new MessageListenerFilter(executorService, messageListeners, this));

            ircChannel.registerMessageFactory(new JoinMessage.Factory());
            ircChannel.registerMessageFactory(new TextMessage.Factory());
            ircChannel.registerMessageFactory(new UnknownCommandMessage.Factory());
        }
    }
}
