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

package com.alesharik.twitch.api.helix;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JsonAdapter(PaginatedArrayList.JsonAdapterFactory.class)
public class PaginatedArrayList<E> extends ArrayList<E> implements PaginatedList<E> {
    @Getter
    protected final String cursor;

    public PaginatedArrayList(int initialCapacity, String cursor) {
        super(initialCapacity);
        this.cursor = cursor;
    }

    public PaginatedArrayList(String cursor) {
        this.cursor = cursor;
    }

    public PaginatedArrayList(Collection<? extends E> c, String cursor) {
        super(c);
        this.cursor = cursor;
    }

    public static final class JsonAdapterFactory implements TypeAdapterFactory {

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if(PaginatedList.class.isAssignableFrom(type.getRawType())) {
                Type c = $Gson$Types.getCollectionElementType(type.getType(), type.getRawType());
                TypeAdapter adapter = gson.getAdapter($Gson$Types.getRawType(c));
                return (TypeAdapter<T>) new JsonAdapterImpl(adapter);
            }
            return null;
        }
    }

    @RequiredArgsConstructor
    static final class JsonAdapterImpl extends TypeAdapter<PaginatedArrayList> {
        private final TypeAdapter adapter;

        @Override
        public void write(JsonWriter out, PaginatedArrayList value) throws IOException {
            out.beginObject();
            if(value.cursor != null)
                out.name("pagination")
                        .beginObject()
                        .name("cursor")
                        .value(value.cursor)
                        .endObject();
            out.name("data");
            out.beginArray();

            for(Object o : value)
                adapter.write(out, o);

            out.endArray();
            out.endObject();
        }

        @Override
        public PaginatedArrayList read(JsonReader in) throws IOException {
            String cursor = null;
            List<Object> list = new ArrayList<>();
            in.beginObject();

            while(in.hasNext()) {
                String name = in.nextName();
                if("data".equals(name)) {
                    in.beginArray();
                    while(in.hasNext())
                        list.add(adapter.read(in));
                    in.endArray();
                }
                if("pagination".equals(name)) {
                    in.beginObject();
                    while(in.hasNext())
                        if("cursor".equals(in.nextName()))
                            cursor = in.nextString();
                    in.endObject();
                }
            }

            in.endObject();
            return new PaginatedArrayList(list, cursor);
        }
    }
}
