package com.readingdiary.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class StaticFileHandler implements HttpHandler {

    private static final Map<String, String> MIME_TYPES = Map.of(
            "css", "text/css; charset=utf-8",
            "js",  "application/javascript; charset=utf-8",
            "png", "image/png",
            "ico", "image/x-icon"
    );

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        String uriPath = exchange.getRequestURI().getPath(); // /static/style.css
        try (InputStream in = StaticFileHandler.class.getResourceAsStream(uriPath)) {
            if (in == null) {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
                return;
            }

            String ext = uriPath.contains(".")
                    ? uriPath.substring(uriPath.lastIndexOf('.') + 1)
                    : "";
            String mimeType = MIME_TYPES.getOrDefault(ext, "application/octet-stream");

            byte[] bytes = in.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", mimeType);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
