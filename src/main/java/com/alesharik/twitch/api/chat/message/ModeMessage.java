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
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ModeMessage implements Message {
    private final String channel;
    private final boolean operator;
    private final String user;

    @Override
    public String toIRCString() {
        return null;
    }

    @Override
    public String getType() {
        return "MODE";
    }

    @Override
    public Direction getUseDirection() {
        return Direction.TO_USER;
    }

    public static final class Factory implements IRCMessageFactory<ModeMessage> {

        @Override
        public ModeMessage newMessage(String msgLine) {
            String[] parts = msgLine.split(" ");
            return new ModeMessage(parts[2].substring(1), parts[3].equals("+0"), parts[4]);
        }

        @Override
        public String getType() {
            return "MODE";
        }
    }
}
