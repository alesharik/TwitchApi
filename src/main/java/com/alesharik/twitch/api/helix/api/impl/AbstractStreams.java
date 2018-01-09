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

import com.alesharik.twitch.api.helix.api.Streams;
import com.alesharik.twitch.api.helix.entity.Stream;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractStreams<StreamMetaPaginatedList, StreamPaginatedList> implements Streams<StreamMetaPaginatedList, StreamPaginatedList> {
    protected static abstract class AbstractGetStreams<StreamList> implements GetStreams<StreamList> {
        protected String after;
        protected String before;
        protected final List<String> communityIds = new ArrayList<>();
        protected int count;
        protected final List<String> games = new ArrayList<>();
        protected final List<String> languages = new ArrayList<>();
        protected Stream.Type type;
        protected final List<String> userIds = new ArrayList<>();
        protected final List<String> userNames = new ArrayList<>();
        
        @Nonnull
        @Override
        public GetStreams<StreamList> after(@Nonnull String cursor) {
            this.after = cursor;
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> before(@Nonnull String cursor) {
            this.before = cursor;
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> addCommunityId(@Nonnull String id) {
            if(this.communityIds.size() > 3)
                throw new IllegalArgumentException("Can't request more than 3 communities!");
            communityIds.add(id);
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> addCommunityIds(@Nonnull String... id) {
            if(this.communityIds.size() + id.length > 3)
                throw new IllegalArgumentException("Can't request more than 3 communities!");
            communityIds.addAll(Arrays.asList(id));
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> count(int count) {
            this.count = count;
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> addGame(@Nonnull String gameId) {
            games.add(gameId);
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> addGames(@Nonnull String... gameId) {
            games.addAll(Arrays.asList(gameId));
            return this;
        }
        @Nonnull
        @Override
        public GetStreams<StreamList> addLanguage(@Nonnull String lang) {
            languages.add(lang);
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> addLanguages(@Nonnull String... lang) {
            languages.addAll(Arrays.asList(lang));
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> type(@Nonnull Stream.Type type) {
            this.type = type;
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> addUserId(@Nonnull String id) {
            userIds.add(id);
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> addUserIds(@Nonnull String... id) {
            userIds.addAll(Arrays.asList(id));
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> addUserLogin(@Nonnull String id) {
            userNames.add(id);
            return this;
        }

        @Nonnull
        @Override
        public GetStreams<StreamList> addUserLogins(@Nonnull String... id) {
            userNames.addAll(Arrays.asList(id));
            return this;
        }
    }
}
