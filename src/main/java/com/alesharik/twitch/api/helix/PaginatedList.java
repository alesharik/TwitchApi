package com.alesharik.twitch.api.helix;

import java.util.List;

public interface PaginatedList extends List {
    String getCursor();
}
