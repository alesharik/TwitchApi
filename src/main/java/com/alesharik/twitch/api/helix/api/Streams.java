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

package com.alesharik.twitch.api.helix.api;

import com.alesharik.twitch.api.helix.entity.Game;
import com.alesharik.twitch.api.helix.entity.User;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.alesharik.twitch.api.helix.entity.Stream.*;

public interface Streams<StreamMeta, StreamPaginatedList> {
    @Nonnull
    GetStreams<StreamPaginatedList> get();

    @Nonnull
    GetStreams<StreamMeta> getMeta();

    interface GetStreams<StreamList> {
        @Nonnull
        GetStreams<StreamList> after(@Nonnull String cursor);

        @Nonnull
        GetStreams<StreamList> before(@Nonnull String cursor);

        @Nonnull
        GetStreams<StreamList> addCommunityId(@Nonnull String id);

        @Nonnull
        GetStreams<StreamList> addCommunityIds(@Nonnull String... id);

        @Nonnull
        GetStreams<StreamList> count(@Nonnegative int count);

        @Nonnull
        GetStreams<StreamList> addGame(@Nonnull String gameId);

        @Nonnull
        default GetStreams<StreamList> addGame(@Nonnull Game game) {
            return addGame(game.getId());
        }

        @Nonnull
        GetStreams<StreamList> addGames(@Nonnull String... gameId);

        @Nonnull
        default GetStreams<StreamList> addGames(@Nonnull Game... game) {
            String[] strings = new String[game.length];
            for(int i = 0; i < game.length; i++)
                strings[i] = game[i].getId();
            return addGames(strings);
        }

        @Nonnull
        GetStreams<StreamList> addLanguage(@Nonnull String lang);

        @Nonnull
        GetStreams<StreamList> addLanguages(@Nonnull String... lang);

        @Nonnull
        GetStreams<StreamList> type(@Nonnull Type type);

        @Nonnull
        GetStreams<StreamList> addUserId(@Nonnull String id);

        @Nonnull
        GetStreams<StreamList> addUserIds(@Nonnull String... id);

        @Nonnull
        default GetStreams<StreamList> addUser(@Nonnull User user) {
            return addUserId(user.getId());
        }

        @Nonnull
        default GetStreams<StreamList> addUsers(@Nonnull User... users) {
            String[] strings = new String[users.length];
            for(int i = 0; i < users.length; i++)
                strings[i] = users[i].getId();
            return addUserIds(strings);
        }

        @Nonnull
        GetStreams<StreamList> addUserLogin(@Nonnull String id);

        @Nonnull
        GetStreams<StreamList> addUserLogins(@Nonnull String... id);

        @Nonnull
        default GetStreams<StreamList> addUserLogin(@Nonnull User user) {
            return addUserLogin(user.getLogin());
        }

        @Nonnull
        default GetStreams<StreamList> addUserLogins(@Nonnull User... users) {
            String[] strings = new String[users.length];
            for(int i = 0; i < users.length; i++)
                strings[i] = users[i].getLogin();
            return addUserLogins(strings);
        }

        @Nonnull
        StreamList get();
    }
}
