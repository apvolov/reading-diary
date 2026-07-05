package com.readingdiary.handler;

import com.readingdiary.dao.UserDao;
import com.readingdiary.model.User;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.PasswordUtil;
import com.readingdiary.util.SessionManager;
import com.readingdiary.util.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoginHandler implements HttpHandler {

    private final UserDao userDao = new UserDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            HttpUtil.sendHtml(exchange, 200, TemplateEngine.render("login.ftl", Map.of()));
            return;
        }

        if (method.equals("POST")) {
            handlePost(exchange);
            return;
        }

        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        Map<String, String> form = HttpUtil.parseFormBody(exchange);
        String username = form.get("username");
        String password = form.get("password");

        try {
            Optional<User> userOpt = userDao.findByUsername(username);

            if (userOpt.isEmpty() || !PasswordUtil.matches(password, userOpt.get().getPasswordHash())) {
                Map<String, Object> model = new HashMap<>();
                model.put("error", "Неверное имя пользователя или пароль");
                HttpUtil.sendHtml(exchange, 401, TemplateEngine.render("login.ftl", model));
                return;
            }

            User user = userOpt.get();
            String sessionId = SessionManager.createSession(user.getId());
            HttpUtil.setSessionCookie(exchange, sessionId);
            HttpUtil.redirect(exchange, "/");
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Ошибка сервера, попробуйте позже");
            HttpUtil.sendHtml(exchange, 500, TemplateEngine.render("login.ftl", model));
        }
    }
}
