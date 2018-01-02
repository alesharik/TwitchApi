package com.alesharik.twitch.api.helix.api;

public interface Users<UserList, UserFollows, User> {
    GetUsers<UserList> get();

    GetFollows<UserFollows> getFollowsFrom(User from);

    GetFollows<UserFollows> getFollowsTo(User to);

    User updateDescription(String newDescription);

    interface GetUsers<UserList> {
        GetUsers<UserList> addId(String id);

        GetUsers<UserList> addIds(String... id);

        GetUsers<UserList> addLogin(String login);

        GetUsers<UserList> addLogins(String... logins);

        UserList get();
    }

    interface GetFollows<UserFollows> {
        GetFollows<UserFollows> after(String cursor);

        GetFollows<UserFollows> before(String cursor);

        GetFollows<UserFollows> count(int count);

        UserFollows get();
    }
}
