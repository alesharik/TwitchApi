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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@JsonAdapter(TemplateImageUrl.TemplateImageUrlJsonAdapter.class)
public final class TemplateImageUrl {
    private final String url;

    public String getUrl(int width, int height) {
        return url.replace("{height}", Integer.toString(height))
                .replace("{width}", Integer.toString(width));
    }

    @Override
    public String toString() {
        return url;
    }

    static final class TemplateImageUrlJsonAdapter extends TypeAdapter<TemplateImageUrl> {

        @Override
        public void write(JsonWriter out, TemplateImageUrl value) throws IOException {
            out.value(value.url);
        }

        @Override
        public TemplateImageUrl read(JsonReader in) throws IOException {
            return new TemplateImageUrl(in.nextString());
        }
    }
}
