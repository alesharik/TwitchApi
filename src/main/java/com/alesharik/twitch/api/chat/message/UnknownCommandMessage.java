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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
public final class UnknownCommandMessage implements Message {
    private final String cmd;

    @Override
    public String toIRCString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType() {
        return "421";
    }

    @Override
    public Direction getUseDirection() {
        return Direction.TO_USER;
    }

    public String getCommand() {
        return cmd;
    }

    public static final class Factory implements IRCMessageFactory<UnknownCommandMessage> {

        @Override
        public UnknownCommandMessage newMessage(String msgLine) {
            String[] messageParts = msgLine.split(" ");
            return new UnknownCommandMessage(messageParts[4]);
        }

        @Override
        public String getType() {
            return "421";
        }
    }
}
