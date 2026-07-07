package com.readingdiary.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipartParser {

    public static class UploadedFile {
        public final String originalFilename;
        public final String contentType;
        public final byte[] data;

        UploadedFile(String originalFilename, String contentType, byte[] data) {
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.data = data;
        }
    }

    private static final int MAX_SIZE = 5 * 1024 * 1024; // 5 MB

    private final Map<String, String> fields = new HashMap<>();
    private UploadedFile uploadedFile;

    private MultipartParser() {}

    public static MultipartParser parse(HttpExchange exchange) throws IOException {
        String contentTypeHeader = exchange.getRequestHeaders().getFirst("Content-Type");
        String boundary = extractBoundary(contentTypeHeader);
        if (boundary == null) {
            throw new IOException("multipart/form-data boundary not found");
        }

        byte[] body = exchange.getRequestBody().readAllBytes();
        if (body.length > MAX_SIZE) {
            throw new IOException("Файл слишком большой (максимум 5 МБ)");
        }

        MultipartParser parser = new MultipartParser();
        parser.doParse(body, boundary);
        return parser;
    }

    public String getField(String name) {
        return fields.get(name);
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    private void doParse(byte[] body, String boundary) {
        byte[] delimiter = ("--" + boundary).getBytes(StandardCharsets.UTF_8);
        byte[] headerSeparator = "\r\n\r\n".getBytes(StandardCharsets.UTF_8);

        List<Integer> positions = findAll(body, delimiter);

        for (int i = 0; i < positions.size() - 1; i++) {
            int partStart = positions.get(i) + delimiter.length + 2; // skip \r\n after boundary
            int partEnd   = positions.get(i + 1) - 2;               // strip \r\n before next boundary

            if (partStart >= partEnd) continue;

            byte[] part = slice(body, partStart, partEnd);

            int sepPos = indexOf(part, headerSeparator, 0);
            if (sepPos < 0) continue;

            String headers = new String(part, 0, sepPos, StandardCharsets.UTF_8);
            byte[] content = slice(part, sepPos + 4, part.length);

            String disposition = null;
            String partContentType = null;
            for (String line : headers.split("\r\n")) {
                String lower = line.toLowerCase();
                if (lower.startsWith("content-disposition:")) {
                    disposition = line;
                } else if (lower.startsWith("content-type:")) {
                    partContentType = line.substring("content-type:".length()).trim();
                }
            }

            if (disposition == null) continue;

            String name     = extractParam(disposition, "name");
            String filename = extractParam(disposition, "filename");

            if (filename != null && !filename.isEmpty()) {
                uploadedFile = new UploadedFile(filename, partContentType, content);
            } else if (name != null) {
                fields.put(name, new String(content, StandardCharsets.UTF_8));
            }
        }
    }

    private static String extractBoundary(String contentType) {
        if (contentType == null) return null;
        for (String part : contentType.split(";")) {
            part = part.trim();
            if (part.startsWith("boundary=")) {
                return part.substring("boundary=".length()).trim();
            }
        }
        return null;
    }

    private static String extractParam(String header, String param) {
        String search = param + "=\"";
        int idx = header.indexOf(search);
        if (idx < 0) return null;
        int start = idx + search.length();
        int end   = header.indexOf('"', start);
        if (end < 0) return null;
        return header.substring(start, end);
    }

    private static List<Integer> findAll(byte[] haystack, byte[] needle) {
        List<Integer> positions = new ArrayList<>();
        outer:
        for (int i = 0; i <= haystack.length - needle.length; i++) {
            for (int j = 0; j < needle.length; j++) {
                if (haystack[i + j] != needle[j]) continue outer;
            }
            positions.add(i);
        }
        return positions;
    }

    private static int indexOf(byte[] haystack, byte[] needle, int from) {
        outer:
        for (int i = from; i <= haystack.length - needle.length; i++) {
            for (int j = 0; j < needle.length; j++) {
                if (haystack[i + j] != needle[j]) continue outer;
            }
            return i;
        }
        return -1;
    }

    private static byte[] slice(byte[] array, int from, int to) {
        byte[] result = new byte[to - from];
        System.arraycopy(array, from, result, 0, result.length);
        return result;
    }
}
