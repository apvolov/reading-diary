package com.readingdiary.handler;

import com.readingdiary.util.CoverStorage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class CoverHandler implements HttpHandler {

    private static final Map<String, String> MIME_TYPES = Map.of(
            "jpg",  "image/jpeg",
            "jpeg", "image/jpeg",
            "png",  "image/png",
            "gif",  "image/gif",
            "webp", "image/webp"
    );

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        String path = exchange.getRequestURI().getPath(); // /covers/42.jpg
        String filename = path.substring(path.lastIndexOf('/') + 1);

        Optional<Path> file = CoverStorage.get(filename);
        if (file.isEmpty()) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
        }

        String ext = filename.contains(".")
                ? filename.substring(filename.lastIndexOf('.') + 1)
                : "";
        String mimeType = MIME_TYPES.getOrDefault(ext, "application/octet-stream");

        byte[] bytes = Files.readAllBytes(file.get());
        exchange.getResponseHeaders().set("Content-Type", mimeType);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
