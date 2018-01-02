package com.alesharik.twitch.api.helix.entity;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.istack.internal.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public final class Stream {
    @SerializedName("id")
    private final String id;
    @SerializedName("user_id")
    private final String userId;
    @SerializedName("game_id")
    private final String gameId;
    @SerializedName("community_ids")
    private final List<String> communityIds;
    @SerializedName("type")
    private final Type type;
    @SerializedName("title")
    private final String title;
    @SerializedName("viewer_count")
    private final int viewerCount;
    @SerializedName("started_at")
    private final Date startedAt;
    @SerializedName("language")
    private final String language;
    @SerializedName("thumbnail_url")
    private final TemplateImageUrl thumbnail;

    @RequiredArgsConstructor
    @Getter
    @JsonAdapter(TypeJsonAdapter.class)
    public enum Type {
        LIVE("live"),
        VODCAST("vodcast"),
        NONE(""),
        /**
         * Only for get streams
         */
        ALL("all");

        private final String name;

        @Nullable
        public static Type forName(@NonNull String name) {
            for(Type type : values()) {
                if(type.name.equals(name))
                    return type;
            }
            return null;
        }
    }

    static final class TypeJsonAdapter extends TypeAdapter<Type> {

        @Override
        public void write(JsonWriter out, Type value) throws IOException {
            out.value(value.getName());
        }

        @Override
        public Type read(JsonReader in) throws IOException {
            return Type.forName(in.nextString());
        }
    }
}