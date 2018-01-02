package com.alesharik.twitch.api.helix.entity;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public final class UserFolllowsPart {
    @SerializedName("pagination")
    private final Pagination pagination;
    @Getter
    @SerializedName("total")
    private final long total;
    @SerializedName("data")
    @Getter
    private final List<Part> follows;

    public String getPagination() {
        return pagination.cursor;
    }

    @EqualsAndHashCode
    @ToString
    @Getter
    @RequiredArgsConstructor
    public static final class Part {
        @SerializedName("from_id")
        private final String fromId;
        @SerializedName("to_id")
        private final String toId;
        @SerializedName("followed_at")
        private final Date followedAt;
    }

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    private static final class Pagination {
        @SerializedName("cursor")
        private final String cursor;
    }
}
