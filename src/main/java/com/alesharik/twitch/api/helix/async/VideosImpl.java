package com.alesharik.twitch.api.helix.async;

import com.alesharik.twitch.api.Auth;
import com.alesharik.twitch.api.helix.PaginatedList;
import com.alesharik.twitch.api.helix.api.impl.AbstractVideos;
import com.alesharik.twitch.api.helix.entity.Game;
import com.alesharik.twitch.api.helix.entity.User;
import com.alesharik.twitch.api.helix.entity.Video;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static com.alesharik.twitch.api.Utils.serializeList;
import static com.alesharik.twitch.api.helix.async.HelixAsync.getValueListExtractor;
import static com.alesharik.twitch.api.helix.async.HelixAsync.newUrlBuilder;

@RequiredArgsConstructor
final class VideosImpl extends AbstractVideos<ResponseFinisher<PaginatedList<Video>>> {
    private final OkHttpClient client;
    private final Auth auth;

    @Nonnull
    @Override
    public GetVideos<ResponseFinisher<PaginatedList<Video>>> getByIds(@Nonnull String... ids) {
        return new GetVideosById(client, auth, ids);
    }

    @Nonnull
    @Override
    public GetVideos<ResponseFinisher<PaginatedList<Video>>> getFromUser(@Nonnull User user) {
        return new GetVideosByUser(client, auth, user.getId());
    }

    @Nonnull
    @Override
    public GetVideos<ResponseFinisher<PaginatedList<Video>>> getForGame(@Nonnull Game game) {
        return new GetVideosByGame(client, auth, game.getId());
    }

    @RequiredArgsConstructor
    private static abstract class GetVideosImpl extends AbstractGetVideos<ResponseFinisher<PaginatedList<Video>>> {
        protected final OkHttpClient client;
        protected final Auth auth;

        protected void fillRequest(HttpUrl.Builder builder) {
            if(after != null)
                builder.addQueryParameter("after", after);
            if(before != null)
                builder.addQueryParameter("before", before);
            if(language != null)
                builder.addQueryParameter("language", language);
            if(period != null)
                builder.addQueryParameter("period", period);
            if(sort != null)
                builder.addQueryParameter("sort", sort);
            if(type != null)
                builder.addQueryParameter("type", type);

            builder.addQueryParameter("first", Integer.toString(count));
        }
    }

    private static final class GetVideosById extends GetVideosImpl {
        private final String[] ids;

        public GetVideosById(OkHttpClient client, Auth auth, String[] ids) {
            super(client, auth);
            this.ids = ids;
        }

        @Nonnull
        @Override
        public ResponseFinisher<PaginatedList<Video>> get() {
            Request.Builder builder = new Request.Builder().get();
            auth.fillRequestHeaders(builder);
            HttpUrl.Builder videos = newUrlBuilder()
                    .addPathSegment("videos");

            if(ids.length < 1)
                throw new IllegalArgumentException("ID count must be >= 1");

            videos.addQueryParameter("id", serializeList(Arrays.asList(ids)));
            fillRequest(videos);
            builder.url(videos.build());
            return getValueListExtractor(builder.build(), client, new TypeToken<PaginatedList<Video>>() {});
        }
    }

    private static final class GetVideosByGame extends GetVideosImpl {
        private final String gameId;

        public GetVideosByGame(OkHttpClient client, Auth auth, String gameId) {
            super(client, auth);
            this.gameId = gameId;
        }

        @Nonnull
        @Override
        public ResponseFinisher<PaginatedList<Video>> get() {
            Request.Builder builder = new Request.Builder().get();
            auth.fillRequestHeaders(builder);
            HttpUrl.Builder videos = newUrlBuilder()
                    .addPathSegment("videos");

            videos.addQueryParameter("game_id", gameId);

            fillRequest(videos);
            builder.url(videos.build());
            return getValueListExtractor(builder.build(), client, new TypeToken<PaginatedList<Video>>() {});
        }
    }

    private static final class GetVideosByUser extends GetVideosImpl {
        private final String userId;

        public GetVideosByUser(OkHttpClient client, Auth auth, String userId) {
            super(client, auth);
            this.userId = userId;
        }

        @Nonnull
        @Override
        public ResponseFinisher<PaginatedList<Video>> get() {
            Request.Builder builder = new Request.Builder().get();
            auth.fillRequestHeaders(builder);
            HttpUrl.Builder videos = newUrlBuilder()
                    .addPathSegment("videos");

            videos.addQueryParameter("user_id", userId);

            fillRequest(videos);
            builder.url(videos.build());
            return getValueListExtractor(builder.build(), client, new TypeToken<PaginatedList<Video>>() {});
        }
    }
}
