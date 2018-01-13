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

package com.alesharik.twitch.api.helix.api.impl;

import com.alesharik.twitch.api.helix.api.Games;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractGames<GameList, GamePaginatedList> implements Games<GameList, GamePaginatedList> {
    protected static abstract class AbstractGetGames<GameList> implements GetGames<GameList> {
        protected final List<String> ids = new ArrayList<>();
        protected final List<String> names = new ArrayList<>();

        @Nonnull
        @Override
        public GetGames<GameList> addId(@Nonnull String id) {
            if(ids.size() == 100)
                throw new IllegalArgumentException("Can't request more than 100 ids!");
            ids.add(id);
            return this;
        }

        @Nonnull
        @Override
        public GetGames<GameList> addIds(@Nonnull String... ids) {
            if(this.ids.size() + ids.length > 100)
                throw new IllegalArgumentException("Can't request more than 100 ids!");
            this.ids.addAll(Arrays.asList(ids));
            return this;
        }

        @Nonnull
        @Override
        public GetGames<GameList> addName(@Nonnull String name) {
            if(names.size() == 100)
                throw new IllegalArgumentException("Can't request more than 100 names!");
            names.add(name);
            return this;
        }

        @Nonnull
        @Override
        public GetGames<GameList> addNames(@Nonnull String... names) {
            if(this.names.size() + names.length > 100)
                throw new IllegalArgumentException("Can't request more than 100 names!");
            this.names.addAll(Arrays.asList(names));
            return this;
        }
    }

    protected static abstract class AbstractGetTopGames<GameList> implements Games.GetTopGames<GameList> {
        protected String after = "";
        protected String before = "";
        protected int count = 20;

        @Nonnull
        @Override
        public Games.GetTopGames<GameList> after(@Nonnull String cursor) {
            after = cursor;
            return this;
        }

        @Nonnull
        @Override
        public Games.GetTopGames<GameList> before(@Nonnull String cursor) {
            before = cursor;
            return this;
        }

        @Nonnull
        @Override
        public Games.GetTopGames<GameList> count(int count) {
            if(count > 100)
                throw new IllegalArgumentException("Can't request more than 100 entries!");
            this.count = count;
            return this;
        }
    }
}
