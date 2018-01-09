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

package com.alesharik.twitch.api.helix.api.impl;

import com.alesharik.twitch.api.helix.api.Videos;

import javax.annotation.Nonnull;

public abstract class AbstractVideos<PaginatedVideoList> implements Videos<PaginatedVideoList> {
    protected abstract static class AbstractGetVideos<VideoList> implements GetVideos<VideoList> {
        protected String after;
        protected String before;
        protected int count;
        protected String language;
        protected String period;
        protected String sort;
        protected String type;

        @Nonnull
        @Override
        public GetVideos<VideoList> after(@Nonnull String cursor) {
            this.after = cursor;
            return this;
        }

        @Nonnull
        @Override
        public GetVideos<VideoList> before(@Nonnull String cursor) {
            this.before = cursor;
            return this;
        }

        @Nonnull
        @Override
        public GetVideos<VideoList> count(int count) {
            this.count = count;
            return this;
        }

        @Nonnull
        @Override
        public GetVideos<VideoList> language(@Nonnull String lang) {
            this.language = lang;
            return this;
        }

        @Nonnull
        @Override
        public GetVideos<VideoList> period(@Nonnull String period) {
            this.period = period;
            return this;
        }

        @Nonnull
        @Override
        public GetVideos<VideoList> sort(@Nonnull String sort) {
            this.sort = sort;
            return this;
        }

        @Nonnull
        @Override
        public GetVideos<VideoList> type(@Nonnull String type) {
            this.type = type;
            return this;
        }
    }
}
