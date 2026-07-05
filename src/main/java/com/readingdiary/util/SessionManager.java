package com.readingdiary.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Хранит соответствие "session id -> id пользователя" в памяти процесса.
 *
 * Важные ограничения такого подхода (это будет решено на следующих этапах):
 * - сессии живут, пока живёт процесс — рестарт сервера разлогинивает всех;
 * - не работает при нескольких инстансах приложения (нет общего хранилища);
 * - на Spring это заменяется на HttpSession (сервлет-контейнер сам
 *   управляет жизненным циклом) либо на внешнее хранилище (Redis) для
 *   горизонтального масштабирования.
 */
public class SessionManager {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Map<String, Long> sessions = new ConcurrentHashMap<>();

    public static String createSession(long userId) {
        byte[] bytes = new byte[24];
        RANDOM.nextBytes(bytes);
        String sessionId = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        sessions.put(sessionId, userId);
        return sessionId;
    }

    public static Long getUserId(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        return sessions.get(sessionId);
    }

    public static void invalidate(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
        }
    }
}
