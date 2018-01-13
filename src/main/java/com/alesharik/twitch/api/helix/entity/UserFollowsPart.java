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
public final class UserFollowsPart {
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
