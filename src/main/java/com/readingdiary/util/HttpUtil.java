package com.readingdiary.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static Map<String, String> parseFormBody(HttpExchange exchange) throws IOException {
        String body = readBody(exchange.getRequestBody());
        return parseUrlEncoded(body);
    }

    public static Map<String, String> parseCookies(HttpExchange exchange) {
        Map<String, String> cookies = new HashMap<>();
        List<String> headers = exchange.getRequestHeaders().get("Cookie");
        if (headers == null) {
            return cookies;
        }
        for (String header : headers) {
            for (String pair : header.split(";")) {
                String[] kv = pair.trim().split("=", 2);
                if (kv.length == 2) {
                    cookies.put(kv[0], kv[1]);
                }
            }
        }
        return cookies;
    }

    public static void setSessionCookie(HttpExchange exchange, String sessionId) {
        exchange.getResponseHeaders().add("Set-Cookie",
                "session_id=" + sessionId + "; Path=/; HttpOnly");
    }

    public static void clearSessionCookie(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Set-Cookie",
                "session_id=; Path=/; HttpOnly; Max-Age=0");
    }

    public static void sendHtml(HttpExchange exchange, int statusCode, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    private static String readBody(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(chunk)) != -1) {
            buffer.write(chunk, 0, bytesRead);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static Map<String, String> parseUrlEncoded(String body) {
        Map<String, String> result = new HashMap<>();
        if (body == null || body.isBlank()) {
            return result;
        }
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            result.put(key, value);
        }
        return result;
    }
}
