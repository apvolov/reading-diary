package com.readingdiary.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public class CoverStorage {

    private static final Path COVERS_DIR;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    static {
        String dir = System.getenv().getOrDefault("COVERS_DIR", "covers");
        COVERS_DIR = Path.of(dir);
        try {
            Files.createDirectories(COVERS_DIR);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Не удалось создать директорию для обложек: " + e.getMessage());
        }
    }

    public static String save(long userBookId, String originalFilename, byte[] data) throws IOException {
        String ext = extension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Недопустимый формат файла. Разрешены: jpg, png, gif, webp");
        }

        // Удаляем старую обложку, если была в другом формате
        for (String oldExt : ALLOWED_EXTENSIONS) {
            Files.deleteIfExists(COVERS_DIR.resolve(userBookId + "." + oldExt));
        }

        String filename = userBookId + "." + ext;
        Files.write(COVERS_DIR.resolve(filename), data);
        return filename;
    }

    public static Optional<Path> get(String filename) {
        if (filename == null || filename.contains("/") || filename.contains("\\") || filename.contains("..")) {
            return Optional.empty();
        }
        Path file = COVERS_DIR.resolve(filename);
        return Files.exists(file) ? Optional.of(file) : Optional.empty();
    }

    private static String extension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot < 0 ? "" : filename.substring(dot + 1).toLowerCase();
    }
}
