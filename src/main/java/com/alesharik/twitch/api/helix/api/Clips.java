package com.alesharik.twitch.api.helix.api;

import com.alesharik.twitch.api.helix.entity.Stream;

public interface Clips<Clip, EditClip, ClipList> {
    Clip getClip(String id);

    EditClip createClip();

    EditClip createClip(String broadcasterId);

    default EditClip createClip(Stream stream) {
        return createClip(stream.getId());
    }
}
