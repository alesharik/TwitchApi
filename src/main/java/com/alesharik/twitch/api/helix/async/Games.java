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
