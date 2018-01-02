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

/**
 *
 * @param <E> element type
 * @param <GameList> list element type
 */
public interface Games<Game, GameList> {
    GetGames<GameList> get();

    GetTopGames<GameList> getTop();

    interface GetGames<GameList> {
        GetGames<GameList> addId(String id);

        GetGames<GameList> addIds(String... ids);

        GetGames<GameList> addName(String name);

        GetGames<GameList> addNames(String... names);

        GameList get();
    }

    interface GetTopGames<GameList> {
        GetTopGames<GameList> after(String cursor);

        GetTopGames<GameList> before(String cursor);

        GetTopGames<GameList> count(int count);

        GameList get();
    }
}
