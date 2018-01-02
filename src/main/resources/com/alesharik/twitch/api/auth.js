var thisPage = location.origin + location.pathname;
var failurePage = location.origin + "/auth-failure.html";

function parseToken() {
    var values = extractAccessToken();
    if (values.access_token != null && values.access_token.length > 0)
        document.location.replace(thisPage + "?access_token=" + values.access_token + "&scope=" + values.scope);
    else
        document.location.replace(failurePage + "?error=access_denied&error_description=no_access_token_found");
}

function extractAccessToken() {
    var values = {};
    var hash = document.location.hash;
    var params = hash.slice(1).split('&');
    for (var i = 0; i < params.length; i++) {
        var param = params[i].split('=');
        if (param[0] === "access_token")
            values.access_token = param[1];
        else if (param[0] === "scope")
            values.scope = param[1];
    }
    return values;
}

window.addEventListener("load", parseToken);