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

public interface Videos<PaginatedVideoList> {
    GetVideos<PaginatedVideoList> getByIds(String... ids);

    GetVideos<PaginatedVideoList> getFromUser(User user);

    GetVideos<PaginatedVideoList> getForGame(Game game);

    interface GetVideos<VideoList> {
        GetVideos<VideoList> after(String cursor);

        GetVideos<VideoList> before(String cursor);

        GetVideos<VideoList> count(int count);

        GetVideos<VideoList> language(String lang);

        /**
         * Period during which the video was created. Valid values: "all", "day", "month", and "week". Default: "all".
         */
        GetVideos<VideoList> period(String period);

        /**
         * Sort order of the videos. Valid values: "time", "trending", and "views". Default: "time".
         */
        GetVideos<VideoList> sort(String sort);

        /**
         * Type of video. Valid values: "all", "upload", "archive", and "highlight". Default: "all".
         */
        GetVideos<VideoList> type(String type);

        VideoList get();
    }
}
