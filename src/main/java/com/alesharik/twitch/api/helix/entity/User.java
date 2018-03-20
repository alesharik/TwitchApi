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

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.IOException;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class User {
    @SerializedName("id")
    private final String id;
    @SerializedName("login")
    private final String login;
    @SerializedName("display_name")
    private final String displayName;
    @SerializedName("description")
    private final String description;
    @SerializedName("broadcaster_type")
    private final BroadcasterType broadcasterType;
    @SerializedName("type")
    private final UserType userType;
    @SerializedName("email")
    private final String email;
    @SerializedName("offline_image_url")
    private final String offlineImageUrl;
    @SerializedName("profile_image_url")
    private final String profileImageUrl;
    @SerializedName("view_count")
    private final long viewCount;

    @JsonAdapter(BroadcasterTypeJsonAdapter.class)
    public enum BroadcasterType {
        PARTNER("partner"),
        AFFILIATE("affiliate"),
        NONE("");

        @Getter
        private final String name;

        BroadcasterType(String name) {
            this.name = name;
        }

        @Nullable
        public static BroadcasterType forName(@NonNull String name) {
            for(BroadcasterType broadcasterType : values()) {
                if(broadcasterType.name.equals(name))
                    return broadcasterType;
            }
            return null;
        }
    }

    public static final class BroadcasterTypeJsonAdapter extends TypeAdapter<BroadcasterType> {

        @Override
        public void write(JsonWriter out, BroadcasterType value) throws IOException {
            out.value(value.getName());
        }

        @Override
        public BroadcasterType read(JsonReader in) throws IOException {
            return BroadcasterType.forName(in.nextString());
        }
    }

    @JsonAdapter(UserTypeJsonAdapter.class)
    public enum UserType {
        STAFF("staff"),
        ADMIN("admin"),
        GLOBAL_MOD("global_mod"),
        NONE("");

        @Getter
        private final String name;

        UserType(String name) {
            this.name = name;
        }

        @Nullable
        public static UserType forName(@NonNull String name) {
            for(UserType type : values()) {
                if(type.name.equals(name))
                    return type;
            }
            return null;
        }
    }

    public static final class UserTypeJsonAdapter extends TypeAdapter<UserType> {

        @Override
        public void write(JsonWriter out, UserType value) throws IOException {
            out.value(value.getName());
        }

        @Override
        public UserType read(JsonReader in) throws IOException {
            return UserType.forName(in.nextString());
        }
    }
}
