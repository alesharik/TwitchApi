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

package com.alesharik.twitch.api.chat.irc;

import com.alesharik.twitch.api.chat.TwitchChatException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class IRCListenerException extends TwitchChatException {
    private final IRCMessage ircMessage;
    private final IRCChannel.Listener listener;

    public IRCListenerException(Throwable cause, IRCMessage message, IRCChannel.Listener listener) {
        super("Exception in listener " + listener + " while processing message " + message, cause);
        this.ircMessage = message;
        this.listener = listener;
    }
}
