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

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class StreamMetadata {
    @SerializedName("user_id")
    private final String userId;
    @SerializedName("game_id")
    private final String gameId;
    @SerializedName("overwatch")
    private final Overwatch overwatch;
    @SerializedName("heartstone")
    private final Heartstone heartstone;

    @RequiredArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static final class Overwatch {
        @SerializedName("broadcaster")
        private final Player broadcaster;

        @RequiredArgsConstructor
        @Getter
        @ToString
        @EqualsAndHashCode
        public static final class Player {
            @SerializedName("hero")
            private final Hero hero;

            @RequiredArgsConstructor
            @Getter
            @ToString
            @EqualsAndHashCode
            public static final class Hero {
                @SerializedName("role")
                private final String role;
                @SerializedName("name")
                private final String name;
                @SerializedName("ability")
                private final String ability;
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static final class Heartstone {
        @SerializedName("broadcaster")
        private final Player broadcaster;
        @SerializedName("opponent")
        private final Player opponent;

        @RequiredArgsConstructor
        @Getter
        @ToString
        @EqualsAndHashCode
        public static final class Player {
            @SerializedName("hero")
            private final Hero hero;

            @RequiredArgsConstructor
            @Getter
            @ToString
            @EqualsAndHashCode
            public static final class Hero {
                @SerializedName("class")
                private final String clazz;
                @SerializedName("type")
                private final String type;
                @SerializedName("name")
                private final String name;
            }
        }
    }
}
