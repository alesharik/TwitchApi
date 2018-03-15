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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

public class BatchException extends RuntimeException {
    protected final List<RuntimeException> exceptions;

    public BatchException(List<RuntimeException> exceptions) {
        this.exceptions = exceptions;
    }

    public BatchException(String message, List<RuntimeException> exceptions) {
        super(message);
        this.exceptions = exceptions;
    }

    @Override
    public Throwable initCause(Throwable cause) {
        return null;
    }

    @Override
    public synchronized Throwable getCause() {
        return null;
    }

    @Override
    public String getMessage() {
        return "Multiple exceptions occur!";
    }

    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        s.println("Exceptions: ");
        for(Exception exception : exceptions)
            exception.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        s.println("Exceptions: ");
        for(Exception exception : exceptions)
            exception.printStackTrace(s);
    }
}
