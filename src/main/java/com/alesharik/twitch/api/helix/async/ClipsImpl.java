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

package com.alesharik.twitch.api.helix.async;

import com.alesharik.twitch.api.Auth;
import com.alesharik.twitch.api.helix.api.impl.AbstractClips;
import com.alesharik.twitch.api.helix.entity.Clip;
import com.alesharik.twitch.api.helix.entity.EditClip;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.annotation.Nonnull;

import static com.alesharik.twitch.api.helix.async.HelixAsync.getValueExtractor;
import static com.alesharik.twitch.api.helix.async.HelixAsync.newUrlBuilder;

@RequiredArgsConstructor
final class ClipsImpl extends AbstractClips<ResponseFinisher<Clip>, ResponseFinisher<EditClip>> {
    private final OkHttpClient client;
    private final Auth auth;

    @Nonnull
    @Override
    public ResponseFinisher<Clip> getClip(@Nonnull String id) {
        Request.Builder builder = new Request.Builder().get();
        auth.fillRequestHeaders(builder);
        builder.url(newUrlBuilder()
                .addPathSegment("clips")
                .addQueryParameter("id", id)
                .build()
        );
        return getValueExtractor(builder.build(), client, Clip.class);
    }

    @Nonnull
    @Override
    public ResponseFinisher<EditClip> createClip() {
        Request.Builder builder = new Request.Builder().get();
        auth.fillRequestHeaders(builder);
        builder.url(newUrlBuilder()
                .addPathSegment("clips")
                .build()
        );

        return getValueExtractor(builder.build(), client, EditClip.class);
    }

    @Nonnull
    @Override
    public ResponseFinisher<EditClip> createClip(@Nonnull String broadcasterId) {
        Request.Builder builder = new Request.Builder().get();
        auth.fillRequestHeaders(builder);
        builder.url(newUrlBuilder()
                .addPathSegment("clips")
                .addQueryParameter("broadcaster_id", broadcasterId)
                .build()
        );
        return getValueExtractor(builder.build(), client, EditClip.class);
    }
}
