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

function extractAccessToken() {
    var values = {};
    var hash = document.location.hash;
    var params = hash.slice(1).split("&");
    for (var i = 0; i < params.length; i++) {
        var param = params[i].split("=");
        if (param[0] === "access_token")
            values.access_token = param[1];
        else if (param[0] === "scope")
            values.scope = param[1];
    }
    return values;
}

function parseToken() {
    var values = extractAccessToken();
    if (values.access_token != null && values.access_token.length > 0)
        document.location.replace(thisPage + "?access_token=" + values.access_token + "&scope=" + values.scope);
    else
        document.location.replace(failurePage + "?error=access_denied&error_description=no_access_token_found");
}

var thisPage = location.origin + location.pathname;
var failurePage = location.origin + "/auth-failure.html";

window.addEventListener("load", parseToken);