package com.alesharik.twitch.api.helix;

import com.alesharik.twitch.api.helix.api.Clips;
import com.alesharik.twitch.api.helix.api.Games;
import com.alesharik.twitch.api.helix.api.Streams;
import com.alesharik.twitch.api.helix.api.Users;
import com.alesharik.twitch.api.helix.api.Videos;

public interface Helix {
    Clips getClips();

    Games getGames();

    Streams getStreams();

    Users getUsers();

    Videos getVideos();
}
