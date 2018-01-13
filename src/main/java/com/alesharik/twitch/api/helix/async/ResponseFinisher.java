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

package com.alesharik.twitch.api.helix.async;

import com.alesharik.twitch.api.ErrorHandler;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class ResponseFinisher<T> {
    private final List<ResponseConsumer<T>> consumers = new ArrayList<>();
    private final Request request;
    private final OkHttpClient client;
    private final ResponseFunction<Response, T> converter;
    private ErrorHandler errorHandler;

    public ResponseFinisher<T> then(ResponseConsumer<T> consumer) {
        consumers.add(consumer);
        return this;
    }

    public ResponseFinisher<T> onError(ErrorHandler handler) {
        this.errorHandler = handler;
        return this;
    }

    public void execute() {
        client.newCall(request).enqueue(new CallbackImpl());
    }

    private final class CallbackImpl implements Callback {

        @Override
        public void onFailure(@Nonnull Call call, @Nonnull IOException e) {
            if(errorHandler != null)
                errorHandler.handle(call, e);
        }

        @Override
        public void onResponse(@Nonnull Call call, @Nonnull Response response) throws IOException {
            T convert = converter.apply(response);
            for(ResponseConsumer<T> consumer : consumers)
                consumer.accept(convert);
        }
    }
}
