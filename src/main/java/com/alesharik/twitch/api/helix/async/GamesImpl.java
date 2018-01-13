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

import com.alesharik.twitch.api.Auth;
import com.alesharik.twitch.api.helix.PaginatedList;
import com.alesharik.twitch.api.helix.api.impl.AbstractGames;
import com.alesharik.twitch.api.helix.entity.Game;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.annotation.Nonnull;
import java.util.List;

import static com.alesharik.twitch.api.Utils.serializeList;
import static com.alesharik.twitch.api.helix.async.HelixAsync.getValueListExtractor;
import static com.alesharik.twitch.api.helix.async.HelixAsync.newUrlBuilder;

@RequiredArgsConstructor
final class GamesImpl extends AbstractGames<ResponseFinisher<List<Game>>, ResponseFinisher<PaginatedList<Game>>> {
    private final OkHttpClient client;
    private final Auth auth;

    @Nonnull
    @Override
    public GetGames<ResponseFinisher<List<Game>>> get() {
        return new GetGamesImpl(auth, client);
    }

    @Nonnull
    @Override
    public GetTopGames<ResponseFinisher<PaginatedList<Game>>> getTop() {
        return new GetTopGamesImpl(auth, client);
    }

    @RequiredArgsConstructor
    private static final class GetGamesImpl extends AbstractGetGames<ResponseFinisher<List<Game>>> {
        private final Auth auth;
        private final OkHttpClient client;

        @Nonnull
        @Override
        public ResponseFinisher<List<Game>> get() {
            if(ids.isEmpty() && names.isEmpty())
                throw new IllegalArgumentException();//TODO illegalRequestException ?

            Request.Builder builder = new Request.Builder().get();
            auth.fillRequestHeaders(builder);
            HttpUrl.Builder games = newUrlBuilder()
                    .addPathSegment("games");

            if(!names.isEmpty())
                games.addQueryParameter("name", serializeList(names));

            if(!ids.isEmpty())
                games.addQueryParameter("id", serializeList(ids));

            builder.url(games.build());
            return getValueListExtractor(builder.build(), client, new TypeToken<List<Game>>() {});
        }
    }

    @RequiredArgsConstructor
    private static final class GetTopGamesImpl extends AbstractGetTopGames<ResponseFinisher<PaginatedList<Game>>> {
        private final Auth auth;
        private final OkHttpClient client;

        @Nonnull
        @Override
        public ResponseFinisher<PaginatedList<Game>> get() {
            Request.Builder builder = new Request.Builder().get();
            auth.fillRequestHeaders(builder);
            HttpUrl.Builder games = newUrlBuilder()
                    .addPathSegment("games")
                    .addPathSegment("top")
                    .addQueryParameter("first", Integer.toString(count));
            if(after != null)
                games.addQueryParameter("after", after);
            if(before != null)
                games.addQueryParameter("before", before);

            builder.url(games.build());
            return getValueListExtractor(builder.build(), client, new TypeToken<PaginatedList<Game>>() {});
        }
    }
}
