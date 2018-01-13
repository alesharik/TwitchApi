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

package com.alesharik.twitch.api;

import com.alesharik.twitch.api.auth.Scope;
import org.junit.Test;

import java.net.URI;

public class TwitchTest {
    @Test
    public void authTest() throws Exception {
        Twitch twitch = new Twitch("ub00uwkym079kr09g8h8oac426ifns");
        twitch.authorizeClient(new URI("http://127.0.0.1:23522/authorize.html"), 23522, Scope.EDIT_USER);
        twitch.async()
                .getGames()
                .getTop()
                .count(100)
                .get()
                .then(games -> games.forEach(game -> {
                    twitch.async()
                            .getVideos()
                            .getForGame(game)
                            .count(100)
                            .get()
                            .then(System.out::print)
                            .execute();
                }))
                .execute();
        Thread.sleep(10000);
    }
}