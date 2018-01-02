package com.alesharik.twitch.api.helix.async;

import com.alesharik.twitch.api.helix.entity.Game;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class Games {
    private final Helix helix;

    public void getTopGames(Consumer<List<Game>> callback, String after, String before) {
        HttpUrl.Builder builder = helix.createUrlBuilder()
                .addPathSegment("games")
                .addPathSegment("top");
        if(!after.isEmpty())
            builder.addQueryParameter("after", after);
        if(!before.isEmpty())
            builder.addQueryParameter("before", before);
        Request request = helix.createRequestBuilder()
                .get()
                .url(builder.build())
                .build();
        helix.callList(request, callback, Game.class);
    }

    public void getTopGames(Consumer<List<Game>> callback, int count) {
        if(count < 0 || count > 100)
            throw new IllegalArgumentException();

        Request request = helix.createRequestBuilder()
                .get()
                .url(helix.createUrlBuilder()
                        .addPathSegment("games")
                        .addPathSegment("top")
                        .addQueryParameter("first", Integer.toString(count))
                        .build())
                .build();
        helix.callList(request, callback, Game.class);
    }

    public void getTopGames(Consumer<List<Game>> callback, String after, String before, int count) {
        if(count < 0 || count > 100)
            throw new IllegalArgumentException();

        HttpUrl.Builder builder = helix.createUrlBuilder()
                .addPathSegment("games")
                .addPathSegment("top");
        if(!after.isEmpty())
            builder.addQueryParameter("after", after);
        if(!before.isEmpty())
            builder.addQueryParameter("before", before);
        Request request = helix.createRequestBuilder()
                .get()
                .url(builder
                        .addQueryParameter("first", Integer.toString(count))
                        .build())
                .build();
        helix.callList(request, callback, Game.class);
    }

    public void getTopGames(Consumer<List<Game>> callback) {
        Request request = helix.createRequestBuilder()
                .get()
                .url(helix.createUrlBuilder()
                        .addPathSegment("games")
                        .addPathSegment("top")
                        .build())
                .build();
        helix.callList(request, callback, Game.class);
    }
}
