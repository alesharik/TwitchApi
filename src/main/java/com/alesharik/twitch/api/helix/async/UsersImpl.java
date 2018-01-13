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
import com.alesharik.twitch.api.helix.api.impl.AbstractUsers;
import com.alesharik.twitch.api.helix.entity.User;
import com.alesharik.twitch.api.helix.entity.UserFollowsPart;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.annotation.Nonnull;
import java.util.List;

import static com.alesharik.twitch.api.Utils.serializeList;
import static com.alesharik.twitch.api.helix.async.HelixAsync.getValueExtractor;
import static com.alesharik.twitch.api.helix.async.HelixAsync.getValueListExtractor;
import static com.alesharik.twitch.api.helix.async.HelixAsync.newUrlBuilder;

@RequiredArgsConstructor
final class UsersImpl extends AbstractUsers<ResponseFinisher<List<User>>, ResponseFinisher<PaginatedList<UserFollowsPart>>, User, ResponseFinisher<User>> {
    private final OkHttpClient client;
    private final Auth auth;

    @Nonnull
    @Override
    public GetUsers<ResponseFinisher<List<User>>> get() {
        return new GetUsersImpl(client, auth);
    }

    @Nonnull
    @Override
    public GetFollows<ResponseFinisher<PaginatedList<UserFollowsPart>>> getFollowsFrom(@Nonnull User from) {
        return new GetFollowsImpl(from.getId(), null, client, auth);
    }

    @Nonnull
    @Override
    public GetFollows<ResponseFinisher<PaginatedList<UserFollowsPart>>> getFollowsTo(@Nonnull User to) {
        return new GetFollowsImpl(null, to.getId(), client, auth);
    }

    @Override
    public ResponseFinisher<User> updateDescription(String newDescription) {
        Request.Builder builder = new Request.Builder().get();
        auth.fillRequestHeaders(builder);
        HttpUrl.Builder url = newUrlBuilder()
                .addPathSegment("users");

        url.addQueryParameter("description", newDescription);

        builder.url(url.build());
        return getValueExtractor(builder.put(RequestBody.create(MediaType.parse("text/plain"), new byte[0])).build(), client, User.class);
    }

    @RequiredArgsConstructor
    private static final class GetUsersImpl extends AbstractGetUsers<ResponseFinisher<List<User>>> {
        private final OkHttpClient client;
        private final Auth auth;

        @Nonnull
        @Override
        public ResponseFinisher<List<User>> get() {
            if(ids.isEmpty() && logins.isEmpty())
            throw new IllegalArgumentException();//TODO illegalRequestException ?

            Request.Builder builder = new Request.Builder().get();
            auth.fillRequestHeaders(builder);
            HttpUrl.Builder url = newUrlBuilder()
                    .addPathSegment("users");

            if(!logins.isEmpty())
                url.addQueryParameter("login", serializeList(logins));

            if(!ids.isEmpty())
                url.addQueryParameter("id", serializeList(ids));

            builder.url(url.build());
            return getValueListExtractor(builder.build(), client, new TypeToken<List<User>>() {});
        }
    }

    @RequiredArgsConstructor//FIXME get total sub count
    private static final class GetFollowsImpl extends AbstractGetFollows<ResponseFinisher<PaginatedList<UserFollowsPart>>> {
        private final String from;
        private final String to;
        private final OkHttpClient client;
        private final Auth auth;

        @Nonnull
        @Override
        public ResponseFinisher<PaginatedList<UserFollowsPart>> get() {
            Request.Builder builder = new Request.Builder().get();
            auth.fillRequestHeaders(builder);
            HttpUrl.Builder url = newUrlBuilder()
                    .addPathSegment("users")
                    .addPathSegment("follows");

            if(from != null)
                url.addQueryParameter("from_id", from);
            else if(to != null)
                url.addQueryParameter("to_id", to);
            else
                throw new IllegalArgumentException("From/to not found!");

            if(after != null)
                url.addQueryParameter("after", after);
            if(before != null)
                url.addQueryParameter("before", before);
            if(count != 20)
                url.addQueryParameter("first", Integer.toString(count));

            builder.url(url.build());
            return getValueListExtractor(builder.build(), client, new TypeToken<PaginatedList<UserFollowsPart>>() {});
        }
    }
}
