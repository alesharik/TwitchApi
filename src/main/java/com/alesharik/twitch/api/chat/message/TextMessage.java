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

package com.alesharik.twitch.api.chat.message;

import com.alesharik.twitch.api.chat.Message;
import com.alesharik.twitch.api.chat.irc.IRCMessageFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class TextMessage implements Message {
    private final String channel;
    private final String message;
    private final String user;

    public TextMessage(String channel, String message) {
        this.channel = channel;
        this.message = message;
        this.user = null;
    }

    private TextMessage(String channel, String message, String user) {
        this.channel = channel;
        this.message = message;
        this.user = user;
    }

    @Override
    public String toIRCString() {
        return "PRIVMSG #" + channel + " :" + message;
    }

    @Override
    public String getType() {
        return "PRIVMSG";
    }

    @Override
    public Direction getUseDirection() {
        return Direction.TWO_SIDED;
    }

    public static final class Factory implements IRCMessageFactory<TextMessage> {

        @Override
        public TextMessage newMessage(String msgLine) {
            String[] line = msgLine.split(" ", 4);
            String user = line[0].substring(1, line[0].indexOf('!'));
            String channel = line[2].substring(1); //#channel - need to delete #
            String message = line[3].substring(1); //:message - need to delete :
            return new TextMessage(channel, message, user);
        }

        @Override
        public String getType() {
            return "PRIVMSG";
        }
    }
}
