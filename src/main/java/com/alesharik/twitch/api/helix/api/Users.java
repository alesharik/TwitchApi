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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface Users<UserList, UserFollows, User, UserReturn> {
    @Nonnull
    GetUsers<UserList> get();

    @Nonnull
    GetFollows<UserFollows> getFollowsFrom(@Nonnull User from);

    @Nonnull
    GetFollows<UserFollows> getFollowsTo(@Nonnull User to);

    UserReturn updateDescription(String newDescription);

    interface GetUsers<UserList> {
        @Nonnull
        GetUsers<UserList> addId(@Nonnull String id);

        @Nonnull
        GetUsers<UserList> addIds(@Nonnull String... id);

        @Nonnull
        GetUsers<UserList> addLogin(@Nonnull String login);

        @Nonnull
        GetUsers<UserList> addLogins(@Nonnull String... logins);

        @Nonnull
        UserList get();
    }

    interface GetFollows<UserFollows> {
        @Nonnull
        GetFollows<UserFollows> after(@Nonnull String cursor);

        @Nonnull
        GetFollows<UserFollows> before(@Nonnull String cursor);

        @Nonnull
        GetFollows<UserFollows> count(@Nonnegative int count);

        @Nonnull
        UserFollows get();
    }
}
