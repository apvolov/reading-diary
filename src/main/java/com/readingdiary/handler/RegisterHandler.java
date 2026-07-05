package com.readingdiary.handler;

import com.readingdiary.dao.UserDao;
import com.readingdiary.model.User;
import com.readingdiary.util.HtmlTemplates;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.PasswordUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class RegisterHandler implements HttpHandler {

    private final UserDao userDao = new UserDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            HttpUtil.sendHtml(exchange, 200, HtmlTemplates.registerForm(null));
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
            HttpUtil.sendHtml(exchange, 400, HtmlTemplates.registerForm("Заполните все поля"));
            return;
        }

        try {
            if (userDao.existsByUsernameOrEmail(username, email)) {
                HttpUtil.sendHtml(exchange, 409, HtmlTemplates.registerForm("Такой пользователь уже существует"));
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
            HttpUtil.sendHtml(exchange, 500, HtmlTemplates.registerForm("Ошибка сервера, попробуйте позже"));
        }
    }
}
