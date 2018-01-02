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

import com.alesharik.twitch.api.exception.http.ForbiddenException;
import com.alesharik.twitch.api.exception.http.InternalServerException;
import com.alesharik.twitch.api.exception.http.NotFoundException;
import com.alesharik.twitch.api.exception.http.RequestNotValidException;
import com.alesharik.twitch.api.exception.http.ServiceUnavailableException;
import com.alesharik.twitch.api.exception.http.TooManyRequestsException;
import com.alesharik.twitch.api.exception.http.UnauthorizedException;
import com.alesharik.twitch.api.exception.http.UnprocessableEntityException;

public final class ErrorHandler {
    public static void handleError(int code) {
        if(code == 400)
            throw new RequestNotValidException();
        else if(code == 401)
            throw new UnauthorizedException();
        else if(code == 403)
            throw new ForbiddenException();
        else if(code == 404)
            throw new NotFoundException();
        else if(code == 422)
            throw new UnprocessableEntityException();
        else if(code == 429)
            throw new TooManyRequestsException();
        else if(code == 500)
            throw new InternalServerException();
        else if(code == 503)
            throw new ServiceUnavailableException();
    }
}
