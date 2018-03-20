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
import com.alesharik.twitch.api.GsonUtils;
import com.alesharik.twitch.api.helix.Helix;
import com.alesharik.twitch.api.helix.PaginatedList;
import com.alesharik.twitch.api.helix.api.Clips;
import com.alesharik.twitch.api.helix.api.Games;
import com.alesharik.twitch.api.helix.api.Streams;
import com.alesharik.twitch.api.helix.api.Users;
import com.alesharik.twitch.api.helix.api.Videos;
import com.alesharik.twitch.api.helix.entity.Clip;
import com.alesharik.twitch.api.helix.entity.EditClip;
import com.alesharik.twitch.api.helix.entity.Game;
import com.alesharik.twitch.api.helix.entity.Stream;
import com.alesharik.twitch.api.helix.entity.StreamMetadata;
import com.alesharik.twitch.api.helix.entity.User;
import com.alesharik.twitch.api.helix.entity.UserFollowsPart;
import com.alesharik.twitch.api.helix.entity.Video;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public final class HelixAsync implements Helix {
    private final ClipsImpl clips;
    private final GamesImpl games;
    private final StreamsImpl streams;
    private final UsersImpl users;
    private final VideosImpl videos;

    public HelixAsync(OkHttpClient client, Auth auth) {
        this.clips = new ClipsImpl(client, auth);
        this.games = new GamesImpl(client, auth);
        this.streams = new StreamsImpl(client, auth);
        this.users = new UsersImpl(client, auth);
        this.videos = new VideosImpl(client, auth);
    }

    @Nonnull
    @Override
    public Clips<ResponseFinisher<Clip>, ResponseFinisher<EditClip>> getClips() {
        return clips;
    }

    @Nonnull
    @Override
    public Games<ResponseFinisher<List<Game>>, ResponseFinisher<PaginatedList<Game>>> getGames() {
        return games;
    }

    @Nonnull
    @Override
    public Streams<ResponseFinisher<PaginatedList<StreamMetadata>>, ResponseFinisher<PaginatedList<Stream>>> getStreams() {
        return streams;
    }

    @Nonnull
    @Override
    public Users<ResponseFinisher<List<User>>, ResponseFinisher<PaginatedList<UserFollowsPart>>, User, ResponseFinisher<User>> getUsers() {
        return users;
    }

    @Nonnull
    @Override
    public Videos<ResponseFinisher<PaginatedList<Video>>> getVideos() {
        return videos;
    }

    static HttpUrl.Builder newUrlBuilder() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.twitch.tv")
                .addPathSegment("helix");
    }

    static <T> ResponseFinisher<T> getValueExtractor(Request request, OkHttpClient client, Class<T> clazz) {
        return new ResponseFinisher<>(request, client, response -> {
            if(handleError(response)) return null;
            ResponseBody body = response.body();
            if(body == null)
                throw new IOException("Response body is null!");
            JsonObject obj = GsonUtils.JSON_PARSER.parse(body.string()).getAsJsonObject();
            JsonArray data = obj.get("data").getAsJsonArray();
            if(data.size() == 0)
                return null;
            return GsonUtils.GSON.fromJson(data.get(0), clazz);
        });
    }

    static <T> ResponseFinisher<T> getValueListExtractor(Request request, OkHttpClient client, TypeToken<T> clazz) {
        return new ResponseFinisher<>(request, client, response -> {
            if(handleError(response))
                return null;
            ResponseBody body = response.body();
            if(body == null)
                throw new IOException("Response body is null!");
            JsonObject obj = GsonUtils.JSON_PARSER.parse(body.string()).getAsJsonObject();

            if(PaginatedList.class.isAssignableFrom(clazz.getRawType()))
                return GsonUtils.GSON.fromJson(obj, clazz.getType());

            JsonArray data = obj.get("data").getAsJsonArray();
            if(data.size() == 0)
                return null;
            return GsonUtils.GSON.fromJson(data, clazz.getType());
        });
    }

    private static boolean handleError(Response response) {
        if(response.code() == 404)
            return true;
        else if(response.code() != 200) {
            System.err.println("Error!"  + response);//FIXME better error handling
            return true;
        }
        return false;
    }
}
