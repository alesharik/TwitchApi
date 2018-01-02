package com.alesharik.twitch.api.helix.async;

import com.alesharik.twitch.api.Auth;
import com.alesharik.twitch.api.helix.RateLimit;
import com.alesharik.twitch.api.helix.UnexpectedResponseException;
import com.alesharik.twitch.api.helix.entity.Clip;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.alesharik.twitch.api.GsonUtils.GSON;
import static com.alesharik.twitch.api.GsonUtils.JSON_PARSER;

public final class Helix {
    private final OkHttpClient httpClient;
    @Getter
    private final RateLimit rateLimit;
    private final Auth auth;

    public Helix(OkHttpClient httpClient, Auth auth) {
        this.httpClient = httpClient;
        this.auth = auth;
        this.rateLimit = new RateLimit();
    }

    public void getClip(Consumer<Clip> clipConsumer, String id) {
        httpClient.newCall(createRequestBuilder()
                .get()
                .url(createUrlBuilder()
                        .addPathSegment("clips")
                        .addQueryParameter("id", id).build())
                .build()).enqueue(new CallbackImpl<>(clipConsumer, Clip.class));
    }

    <T> List<T> parseDataResponse(Response response, Class<T> clazz) {
        parseLimitHeaders(response);
        String body;
        try {
            ResponseBody b = response.body();
            if(b == null)
                throw new UnexpectedResponseException("Response body is empty");
            body = b.string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject obj = JSON_PARSER.parse(body).getAsJsonObject();
        JsonArray data = obj.get("data").getAsJsonArray();
        List<T> list = new ArrayList<>();
        for(int i = 0; i < data.size(); i++)
            list.add(GSON.fromJson(data.get(i).getAsJsonObject(), clazz));
        return list;
    }

    void parseLimitHeaders(Response response) {
        String rl = response.header("RateLimit-Limit");
        if(rl == null)
            throw new UnexpectedResponseException("RateLimit-Limit header not found!");
        int limit = Integer.parseInt(rl);
        String rr = response.header("RateLimit-Remaining");
        if(rr == null)
            throw new UnexpectedResponseException("RateLimit-Remaining header not found!");
        int remaining = Integer.parseInt(rr);
        String rre = response.header("RateLimit-Reset");
        if(rre == null)
            throw new UnexpectedResponseException("RateLimit-Reset header not found!");
        long reset = Long.parseLong(rre);
//        rateLimit.update(limit, remaining, reset);
    }

    Request.Builder createRequestBuilder() {
        Request.Builder builder = new Request.Builder();
        if(auth.getToken() != null)
            builder.addHeader("Authorization", "Bearer " + auth.getToken());
        builder.addHeader("Client-ID", auth.getClientId());
        return builder;
    }

    HttpUrl.Builder createUrlBuilder() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.twitch.tv")
                .addPathSegment("helix");
    }

    <T> void call(Request request, Consumer<T> consumer, Class<T> clazz) {
        httpClient.newCall(request).enqueue(new Helix.CallbackImpl<>(consumer, clazz));
    }

    <T> void callList(Request request, Consumer<List<T>> consumer, Class<T> clazz) {
        httpClient.newCall(request).enqueue(new Helix.ListCallbackImpl<>(consumer, clazz));
    }

    @RequiredArgsConstructor
    final class CallbackImpl<T> implements Callback {
        private final Consumer<T> consumer;
        private final Class<T> clazz;

        @Override
        public void onFailure(@Nonnull Call call, @Nonnull IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(@Nonnull Call call, @Nonnull Response response) {
            consumer.accept(parseDataResponse(response, clazz).get(0));
        }
    }

    @RequiredArgsConstructor
    final class ListCallbackImpl<T> implements Callback {
        private final Consumer<List<T>> consumer;
        private final Class<T> clazz;

        @Override
        public void onFailure(@Nonnull Call call, @Nonnull IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(@Nonnull Call call, @Nonnull Response response) {
            consumer.accept(parseDataResponse(response, clazz));
        }
    }
}
