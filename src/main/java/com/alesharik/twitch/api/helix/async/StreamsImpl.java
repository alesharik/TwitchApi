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
import com.alesharik.twitch.api.helix.api.impl.AbstractStreams;
import com.alesharik.twitch.api.helix.entity.Stream;
import com.alesharik.twitch.api.helix.entity.StreamMetadata;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.annotation.Nonnull;

import static com.alesharik.twitch.api.Utils.serializeList;
import static com.alesharik.twitch.api.helix.async.HelixAsync.getValueListExtractor;
import static com.alesharik.twitch.api.helix.async.HelixAsync.newUrlBuilder;

@RequiredArgsConstructor
final class StreamsImpl extends AbstractStreams<ResponseFinisher<PaginatedList<StreamMetadata>>, ResponseFinisher<PaginatedList<Stream>>> {
    private final OkHttpClient client;
    private final Auth auth;

    @Nonnull
    @Override
    public GetStreams<ResponseFinisher<PaginatedList<Stream>>> get() {
        return new GetStreamsImpl(client, auth);
    }

    @Nonnull
    @Override
    public GetStreams<ResponseFinisher<PaginatedList<StreamMetadata>>> getMeta() {
        return new GetStreamsMetaImpl(client, auth);
    }

    @RequiredArgsConstructor
    private abstract static class AbstractGetStreamsAsync<E> extends AbstractGetStreams<ResponseFinisher<PaginatedList<E>>> {
        protected final OkHttpClient client;
        protected final Auth auth;

        Request.Builder preGet() {
            Request.Builder builder = new Request.Builder().get();
            auth.fillRequestHeaders(builder);
            HttpUrl.Builder streams = newUrlBuilder()
                    .addPathSegment("streams");

            if(after != null)
                streams.addQueryParameter("after", after);
            if(before != null)
                streams.addQueryParameter("before", before);
            if(!communityIds.isEmpty())
                streams.addQueryParameter("community_id", serializeList(communityIds));
            if(!games.isEmpty())
                streams.addQueryParameter("game_id", serializeList(games));
            if(!languages.isEmpty())
                streams.addQueryParameter("language", serializeList(languages));
            if(!userIds.isEmpty())
                streams.addQueryParameter("user_id", serializeList(userIds));
            if(!userNames.isEmpty())
                streams.addQueryParameter("user_login", serializeList(userNames));

            streams.addQueryParameter("first", Integer.toString(count));
            streams.addQueryParameter("type", type.getName());

            builder.url(streams.build());
            return builder;
        }
    }

    private static final class GetStreamsMetaImpl extends AbstractGetStreamsAsync<StreamMetadata> {
        GetStreamsMetaImpl(OkHttpClient client, Auth auth) {
            super(client, auth);
        }

        @Nonnull
        @Override
        public ResponseFinisher<PaginatedList<StreamMetadata>> get() {
            Request.Builder builder = preGet();
            return getValueListExtractor(builder.build(), client, new TypeToken<PaginatedList<StreamMetadata>>() {});
        }
    }

    private static final class GetStreamsImpl extends AbstractGetStreamsAsync<Stream> {
        GetStreamsImpl(OkHttpClient client, Auth auth) {
            super(client, auth);
        }

        @Nonnull
        @Override
        public ResponseFinisher<PaginatedList<Stream>> get() {
            Request.Builder builder = preGet();
            return getValueListExtractor(builder.build(), client, new TypeToken<PaginatedList<Stream>>() {});
        }
    }
}
