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
