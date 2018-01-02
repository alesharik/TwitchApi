package com.alesharik.twitch.api;

import com.alesharik.twitch.api.auth.Scope;
import org.junit.Test;

import java.net.URI;

public class TwitchTest {
    @Test
    public void authTest() throws Exception {
        Twitch twitch = new Twitch("ub00uwkym079kr09g8h8oac426ifns");
        twitch.authorizeClient(new URI("http://127.0.0.1:23522/authorize.html"), 23522, Scope.EDIT_USER);
        twitch.getHelix().getTopGames(System.out::println);
        Thread.sleep(10000);
    }
}