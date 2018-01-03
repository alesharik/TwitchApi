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

package com.alesharik.twitch.api.helix.api;

import com.alesharik.twitch.api.helix.entity.Game;
import com.alesharik.twitch.api.helix.entity.User;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface Videos<PaginatedVideoList> {
    @Nonnull
    GetVideos<PaginatedVideoList> getByIds(@Nonnull String... ids);

    @Nonnull
    GetVideos<PaginatedVideoList> getFromUser(@Nonnull User user);

    @Nonnull
    GetVideos<PaginatedVideoList> getForGame(@Nonnull Game game);

    interface GetVideos<VideoList> {
        @Nonnull
        GetVideos<VideoList> after(@Nonnull String cursor);

        @Nonnull
        GetVideos<VideoList> before(@Nonnull String cursor);

        @Nonnull
        GetVideos<VideoList> count(@Nonnegative int count);

        @Nonnull
        GetVideos<VideoList> language(@Nonnull String lang);

        /**
         * Period during which the video was created. Valid values: "all", "day", "month", and "week". Default: "all".
         */
        @Nonnull
        GetVideos<VideoList> period(@Nonnull String period);

        /**
         * Sort order of the videos. Valid values: "time", "trending", and "views". Default: "time".
         */
        @Nonnull
        GetVideos<VideoList> sort(@Nonnull String sort);

        /**
         * Type of video. Valid values: "all", "upload", "archive", and "highlight". Default: "all".
         */
        @Nonnull
        GetVideos<VideoList> type(@Nonnull String type);

        @Nonnull
        VideoList get();
    }
}
