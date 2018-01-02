package com.alesharik.twitch.api.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
final class AuthCallbackRequestHttpHandler {
    private final URL auth;
    private final URL success;
    private final URL failure;
    private final Socket socket;

    @Nonnull
    public Response process() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String[] request = bufferedReader.readLine().split(" ");
            String addr = request[1];
            String httpVersion = request[2];
            List<String> headers = new ArrayList<>();
            String h;
            while(!(h = bufferedReader.readLine()).isEmpty())
                headers.add(h);
            String host = "127.0.0.1";
            for(String header : headers) {
                if(header.startsWith("Host"))
                    host = header.replace("Host: ", "");
            }
            HttpUrl url = HttpUrl.parse("http://" + host + addr.replace("#", "?"));
            assert url != null;
            String state = url.queryParameter("state");
            if(state == null)
                state = "";
            String token = url.queryParameter("access_token");

            String error = url.queryParameter("error");
            String errorDescription = url.queryParameter("error_description");

            StringBuilder path = new StringBuilder();
            for(String s : url.pathSegments()) {
                path.append('/');
                path.append(s);
            }
            String p = path.toString();
            InputStream stream;
            String contentType;
            if(!p.endsWith("js") && !p.endsWith("css")) {
                if(token != null)
                    stream = success.openStream();
                else if(error != null)
                    stream = failure.openStream();
                else
                    stream = auth.openStream();
                contentType = "Content-Type: text/html\r\n";
            } else {
                contentType = p.endsWith("js") ? "Content-Type: application/javascript\r\n" : "Content-type: text/css\r\n";
                stream = this.getClass().getResourceAsStream(p);
            }

            boolean ok = stream != null;
            String status = httpVersion + (ok ? " 200 OK\r\n" : " 404 Not Found\r\n");
            OutputStream out = socket.getOutputStream();
            out.write(status.getBytes());
            String responseHeaders = contentType +
                    "Content-Length: " + (ok ? stream.available() : 0) + "\r\n" +
                    "Server: Twitch Callback Server\r\n" +
                    "Connection: close\r\n\r\n";
            out.write(responseHeaders.getBytes());

            if(ok) {
                byte[] buf = new byte[1024];
                int nRead;
                while((nRead = stream.read(buf)) == 1024)
                    out.write(buf, 0, nRead);
                out.write(buf, 0, nRead);
                out.flush();
            }

            out.close();
            socket.close();

            if(token != null)
                return new SuccessResponse(state, token);
            else if(error != null)
                return new ErrorResponse(state, error, errorDescription);
            else
                return new EmptyResponse();
        }
    }

    @RequiredArgsConstructor
    public abstract static class Response {
        private final String state;

        public abstract boolean isSuccess();

        public abstract boolean isError();
    }

    @Getter
    public final static class SuccessResponse extends Response {
        private final String token;

        public SuccessResponse(String nonce, String token) {
            super(nonce);
            this.token = token;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isError() {
            return false;
        }
    }

    @Getter
    public static final class ErrorResponse extends Response {
        private final String error;
        private final String description;

        public ErrorResponse(String nonce, String error, String description) {
            super(nonce);
            this.error = error;
            this.description = description;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isError() {
            return true;
        }
    }

    public static final class EmptyResponse extends Response {

        public EmptyResponse() {
            super("");
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isError() {
            return false;
        }
    }
}
