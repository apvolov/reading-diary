package com.readingdiary.handler;

import com.readingdiary.dao.UserDao;
import com.readingdiary.model.User;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.PasswordUtil;
import com.readingdiary.util.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterHandler implements HttpHandler {

    private final UserDao userDao = new UserDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            HttpUtil.sendHtml(exchange, 200, TemplateEngine.render("register.ftl", Map.of()));
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
        String email = form.get("email");
        String password = form.get("password");

        if (username == null || username.isBlank() || email == null || password == null || password.isBlank()) {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Заполните все поля");
            HttpUtil.sendHtml(exchange, 400, TemplateEngine.render("register.ftl", model));
            return;
        }

        try {
            if (userDao.existsByUsernameOrEmail(username, email)) {
                Map<String, Object> model = new HashMap<>();
                model.put("error", "Такой пользователь уже существует");
                HttpUtil.sendHtml(exchange, 409, TemplateEngine.render("register.ftl", model));
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(PasswordUtil.hash(password));
            userDao.create(user);

            HttpUtil.redirect(exchange, "/login");
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Ошибка сервера, попробуйте позже");
            HttpUtil.sendHtml(exchange, 500, TemplateEngine.render("register.ftl", model));
        }
    }
}
