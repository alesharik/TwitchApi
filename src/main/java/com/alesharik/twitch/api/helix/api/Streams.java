package com.alesharik.twitch.api.helix.api;

import com.alesharik.twitch.api.helix.entity.Game;
import com.alesharik.twitch.api.helix.entity.User;

import static com.alesharik.twitch.api.helix.entity.Stream.*;

public interface Streams<StreamMeta, StreamPaginatedList> {
    GetStreams<StreamPaginatedList> get();

    GetStreams<StreamMeta> getMeta();

    interface GetStreams<StreamList> {
        GetStreams<StreamList> after(String cursor);

        GetStreams<StreamList> before(String cursor);

        GetStreams<StreamList> addCommunityId(String id);

        GetStreams<StreamList> addCommunityIds(String... id);

        GetStreams<StreamList> count(int count);

        GetStreams<StreamList> addGame(String gameId);

        default GetStreams<StreamList> addGame(Game game) {
            return addGame(game.getId());
        }

        GetStreams<StreamList> addGames(String... gameId);

        default GetStreams<StreamList> addGames(Game... game) {
            String[] strings = new String[game.length];
            for(int i = 0; i < game.length; i++)
                strings[i] = game[i].getId();
            return addGames(strings);
        }

        GetStreams<StreamList> addLanguage(String lang);

        GetStreams<StreamList> addLanguages(String... lang);

        GetStreams<StreamList> type(Type type);

        GetStreams<StreamList> addUserId(String id);

        GetStreams<StreamList> addUserIds(String... id);

        default GetStreams<StreamList> addUser(User user) {
            return addUserId(user.getId());
        }

        default GetStreams<StreamList> addUsers(User... users) {
            String[] strings = new String[users.length];
            for(int i = 0; i < users.length; i++)
                strings[i] = users[i].getId();
            return addUserIds(strings);
        }

        GetStreams<StreamList> addUserLogin(String id);

        GetStreams<StreamList> addUserLogins(String... id);

        default GetStreams<StreamList> addUserLogin(User user) {
            return addUserLogin(user.getLogin());
        }

        default GetStreams<StreamList> addUserLogins(User... users) {
            String[] strings = new String[users.length];
            for(int i = 0; i < users.length; i++)
                strings[i] = users[i].getLogin();
            return addUserLogins(strings);
        }

        StreamList get();
    }
}
