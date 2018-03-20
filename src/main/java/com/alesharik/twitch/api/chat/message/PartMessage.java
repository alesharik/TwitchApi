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
import lombok.Getter;

@Getter
public final class PartMessage implements Message {
    private final String channel;
    private final String user;

    private PartMessage(String channel, String user) {
        this.channel = channel;
        this.user = user;
    }

    public PartMessage(String channel) {
        this.channel = channel;
        this.user = null;
    }

    @Override
    public String toIRCString() {
        return "PART #" + channel;
    }

    @Override
    public String getType() {
        return "PART";
    }

    @Override
    public Direction getUseDirection() {
        return Direction.TWO_SIDED;
    }

    public static final class Factory implements IRCMessageFactory<PartMessage> {

        @Override
        public PartMessage newMessage(String msgLine) {
            String[] line = msgLine.split(" ");
            String user = line[0].substring(1, line[0].indexOf('!'));
            return new PartMessage(line[2], user);
        }

        @Override
        public String getType() {
            return "PART";
        }
    }
}
