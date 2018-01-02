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
