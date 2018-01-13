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

import com.alesharik.twitch.api.helix.api.Users;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractUsers<UserList, UserFollows, User, UserReturn> implements Users<UserList, UserFollows, User, UserReturn> {
    protected static abstract class AbstractGetUsers<UserList> implements GetUsers<UserList> {
        protected final List<String> ids = new ArrayList<>();
        protected final List<String> logins = new ArrayList<>();

        @Nonnull
        @Override
        public GetUsers<UserList> addId(@Nonnull String id) {
            if(this.ids.size() > 100)
                throw new IllegalArgumentException("Can't request more than 100 user ids!");
            ids.add(id);
            return this;
        }

        @Nonnull
        @Override
        public GetUsers<UserList> addIds(@Nonnull String... id) {
            if(this.ids.size() + id.length > 100)
                throw new IllegalArgumentException("Can't request more than 100 user ids!");
            ids.addAll(Arrays.asList(id));
            return this;
        }

        @Nonnull
        @Override
        public GetUsers<UserList> addLogin(@Nonnull String login) {
            if(this.logins.size() > 100)
                throw new IllegalArgumentException("Can't request more than 100 user logins!");
            logins.add(login);
            return this;
        }

        @Nonnull
        @Override
        public GetUsers<UserList> addLogins(@Nonnull String... logins) {
            if(this.logins.size() + logins.length > 100)
                throw new IllegalArgumentException("Can't request more than 100 user logins!");
            this.logins.addAll(Arrays.asList(logins));
            return this;
        }
    }

    protected abstract static class AbstractGetFollows<UserFollows> implements GetFollows<UserFollows> {
        protected String after;
        protected String before;
        protected int count = 20;

        @Nonnull
        @Override
        public GetFollows<UserFollows> after(@Nonnull String cursor) {
            this.after = cursor;
            return this;
        }

        @Nonnull
        @Override
        public GetFollows<UserFollows> before(@Nonnull String cursor) {
            this.before = cursor;
            return this;
        }

        @Nonnull
        @Override
        public GetFollows<UserFollows> count(int count) {
            if(count > 100)
                throw new IllegalArgumentException("Can't request more than 100 entries!");
            this.count = count;
            return this;
        }
    }
}
