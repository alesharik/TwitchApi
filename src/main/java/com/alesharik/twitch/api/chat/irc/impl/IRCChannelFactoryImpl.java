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

package com.alesharik.twitch.api.chat.irc.impl;

import com.alesharik.twitch.api.BatchException;
import com.alesharik.twitch.api.chat.TwitchChatException;
import com.alesharik.twitch.api.chat.irc.IRCChannel;
import com.alesharik.twitch.api.chat.irc.IRCChannelFactory;
import com.alesharik.twitch.api.chat.irc.IRCListenerException;
import com.alesharik.twitch.api.chat.irc.IRCMessage;
import com.alesharik.twitch.api.chat.irc.IRCMessageFactory;
import com.alesharik.twitch.api.chat.irc.SocketClosedException;
import lombok.AllArgsConstructor;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public final class IRCChannelFactoryImpl implements IRCChannelFactory {
    public IRCChannelImpl create(@Nonnull InetAddress host, @Nonnegative int port, boolean secure, @Nonnull Consumer<Runnable> starter, @Nonnull ExecutorService executorService) throws IOException {
        Socket socket;
        if(secure)
            socket = SSLSocketFactory.getDefault().createSocket(host, port);
        else
            socket = new Socket(host, port);
        IRCChannelImpl channel = new IRCChannelImpl(socket, executorService);
        starter.accept(channel);
        return channel;
    }

    private static final class IRCChannelImpl implements IRCChannel, Runnable {
        private final Socket socket;
        private final StringBuilder stringBuffer;

        private final ExecutorService executor;

        private final List<Listener> listeners;
        private final Map<String, IRCMessageFactory> factories;

        IRCChannelImpl(Socket socket, ExecutorService executorService) {
            this.socket = socket;
            this.stringBuffer = new StringBuilder();
            this.executor = executorService;
            this.listeners = new CopyOnWriteArrayList<>();
            this.factories = new ConcurrentHashMap<>();
        }

        @Override
        public void sendMessage(IRCMessage message) {
            if(socket.isClosed() || socket.isOutputShutdown() || !socket.isConnected())
                throw new SocketClosedException();
            byte[] data = message
                    .toIRCString()
                    .concat("\n")
                    .getBytes(StandardCharsets.UTF_8);
            try {
                socket.getOutputStream().write(data);
                socket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void registerMessageFactory(IRCMessageFactory factory) {
            factories.put(factory.getType(), factory);
        }

        @Override
        public void unregisterMessageFactory(IRCMessageFactory factory) {
            factories.remove(factory.getType(), factory);
        }

        @Override
        public void registerListener(Listener listener) {
            listeners.add(listener);
        }

        @Override
        public void unregisterListener(Listener listener) {
            listeners.remove(listener);
        }

        @Override
        public void shutdown() {
            try {
                socket.close();
            } catch (IOException e) {
                throw new TwitchChatException(e);
            }
        }

        @Override
        public void run() {
            while(!socket.isInputShutdown() && !socket.isClosed()) {
                try {
                    while(!socket.isConnected())
                        Thread.sleep(1);
                    InputStream stream = socket.getInputStream();
                    InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                    int n;
                    while((n = reader.read()) != -1) {
                        char c = (char) n;
                        if(c == '\n' || c == '\r') {
                            if(stringBuffer.length() > 0) {
                                String msg = stringBuffer.toString();
                                publishMessage(msg);
                                stringBuffer.delete(0, stringBuffer.length());
                            }
                        } else
                            stringBuffer.append(c);
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException ignored) {
                }
                System.out.println("Retrying to read in 10 ms");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void publishMessage(String msg) {
            if("PING :tmi.twitch.tv".equals(msg)) {
                try {
                    socket.getOutputStream().write("PONG :tmi.twitch.tv\n".getBytes(StandardCharsets.UTF_8));
                    socket.getOutputStream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            String removeStuff = msg.substring(msg.indexOf(':'));
            String startWithoutUser = removeStuff.substring(removeStuff.indexOf(' ') + 1); //Cut :blablabla part
            String cmd = startWithoutUser.substring(0, startWithoutUser.indexOf(' '));
            IRCMessageFactory factory = factories.get(cmd);
            if(factory != null)
                executor.execute(new ProcessTask(msg, factory, message -> executor.execute(new ListenTask(message, listeners))));
            else if(!cmd.matches("[0-9]+"))
                System.err.println("[IRC] Unexpected message " + msg);
        }

        @AllArgsConstructor
        private static final class ProcessTask implements Runnable {
            private final String message;
            private final IRCMessageFactory messageFactory;
            private final Consumer<IRCMessage> after;

            @Override
            public void run() {
                IRCMessage msg = messageFactory.newMessage(message);
                after.accept(msg);
            }
        }

        @AllArgsConstructor
        private static final class ListenTask implements Runnable {
            private final IRCMessage message;
            private final List<Listener> listeners;

            @Override
            public void run() {
                List<RuntimeException> exceptions = new ArrayList<>();
                for(Listener listener : listeners) {
                    try {
                        listener.listen(message);
                    } catch (Exception e) {
                        exceptions.add(new IRCListenerException(e, message, listener));
                    }
                }
                if(exceptions.isEmpty())
                    return;
                if(exceptions.size() == 1)
                    throw exceptions.get(0);
                throw new BatchException(exceptions);
            }
        }
    }
}
