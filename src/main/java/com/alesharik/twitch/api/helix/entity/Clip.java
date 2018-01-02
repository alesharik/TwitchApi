package com.alesharik.twitch.api.helix.entity;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class Clip {
    @SerializedName("id")
    private final String id;
    @SerializedName("url")
    private final String url;
    @SerializedName("embed_url")
    private final String embedUrl;
    @SerializedName("broadcaster_id")
    private final String broadcasterId;
    @SerializedName("creator_id")
    private final String creatorId;
    @SerializedName("video_id")
    private final String videoId;
    @SerializedName("game_id")
    private final String gameId;
    @SerializedName("language")
    private final String language;
    @SerializedName("title")
    private final String title;
    @SerializedName("view_count")
    private final long viewCount;
    @SerializedName("created_at")
    private final Date createdAt;
    @SerializedName("thumbnail_id")
    private final String thumbnailUrl;
}
