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
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Iterator;

@RequiredArgsConstructor
public final class RequestCapabilityMessage implements Message {
    private final Capability capability;

    @Override
    public String toIRCString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = Arrays.asList(capability.names).iterator();
        while(iterator.hasNext()) {
            String next = iterator.next();

            stringBuilder.append("CAP REQ :twitch.tv/");
            stringBuilder.append(next);
            if(iterator.hasNext())
                stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public String getType() {
        return "CAP";
    }

    @Override
    public Direction getUseDirection() {
        return Direction.TO_SERVER;
    }

    @RequiredArgsConstructor
    public enum Capability {
        /**
         * Notifications JOIN/PART, MODE, etc
         */
        MEMBRESHIP(new String[]{"membership"}),
        TAGS(new String[]{"tags"}),
        COMMANDS(new String[]{"commands"}),
        CHAT_ROOMS(new String[]{"tags", "commands"});

        private final String[] names;
    }
}
