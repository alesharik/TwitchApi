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
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class Video {
    @SerializedName("id")
    private final String id;
    @SerializedName("user_id")
    private final String userId;
    @SerializedName("title")
    private final String title;
    @SerializedName("description")
    private final String description;
    @SerializedName("created_at")
    private final Date createdAt;
    @SerializedName("published_at")
    private final Date publishedAt;
    @SerializedName("thumbnail_url")
    private final TemplateImageUrl thumbnailUrl;
    @SerializedName("view_count")
    private final long viewCount;
    @SerializedName("language")
    private final String language;
    @SerializedName("duration")
    @JsonAdapter(DurationJsonAdapter.class)
    private final Duration duration;

    private static final class DurationJsonAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(JsonWriter out, Duration value) throws IOException {
            long h = value.toHours();
            value = value.minusHours(h);
            long m = value.toMinutes();
            value = value.minusMinutes(m);
            long s = value.getSeconds();
            StringBuilder stringBuilder = new StringBuilder();
            if(h > 0) {
                stringBuilder.append(h);
                stringBuilder.append('h');
            }
            if(m > 0) {
                stringBuilder.append(m);
                stringBuilder.append('m');
            }
            stringBuilder.append(s);
            stringBuilder.append('s');
            out.value(stringBuilder.toString());
        }

        @Override
        public Duration read(JsonReader in) throws IOException {
            String parse = in.nextString();
            int hIndex = parse.indexOf('h');
            long h = 0;
            if(hIndex != -1) {
                h = Long.parseLong(parse.substring(0, hIndex));
                parse = parse.substring(hIndex + 1);
            }
            int mIndex = parse.indexOf('m');
            long m = 0;
            if(mIndex != -1) {
                m = Long.parseLong(parse.substring(0, mIndex));
                parse = parse.substring(mIndex + 1);
            }
            int sIndex = parse.indexOf('s');
            long s = 0;
            if(sIndex != -1)
                s = Long.parseLong(parse.substring(0, sIndex));
            return Duration.ofSeconds(s)
                    .plusMinutes(m)
                    .plusHours(h);
        }
    }
}
