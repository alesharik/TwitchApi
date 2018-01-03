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

/**
 *
 * @param <GameList> list element type
 */
public interface Games<GameList> {
    @Nonnull
    GetGames<GameList> get();

    @Nonnull
    GetTopGames<GameList> getTop();

    interface GetGames<GameList> {
        @Nonnull
        GetGames<GameList> addId(@Nonnull String id);

        @Nonnull
        GetGames<GameList> addIds(@Nonnull String... ids);

        @Nonnull
        GetGames<GameList> addName(@Nonnull String name);

        @Nonnull
        GetGames<GameList> addNames(@Nonnull String... names);

        @Nonnull
        GameList get();
    }

    interface GetTopGames<GameList> {
        @Nonnull
        GetTopGames<GameList> after(@Nonnull String cursor);

        @Nonnull
        GetTopGames<GameList> before(@Nonnull String cursor);

        @Nonnull
        GetTopGames<GameList> count(@Nonnegative int count);

        @Nonnull
        GameList get();
    }
}
