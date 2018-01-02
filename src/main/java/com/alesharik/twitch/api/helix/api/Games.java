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
