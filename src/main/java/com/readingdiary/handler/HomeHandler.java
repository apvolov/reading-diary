package com.readingdiary.handler;

import com.readingdiary.dao.UserBookDao;
import com.readingdiary.dao.UserDao;
import com.readingdiary.model.User;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.SessionManager;
import com.readingdiary.util.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HomeHandler implements HttpHandler {

    private final UserDao userDao = new UserDao();
    private final UserBookDao userBookDao = new UserBookDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> cookies = HttpUtil.parseCookies(exchange);
        Long userId = SessionManager.getUserId(cookies.get("session_id"));

        if (userId == null) {
            HttpUtil.redirect(exchange, "/login");
            return;
        }

        try {
            Optional<User> userOpt = userDao.findById(userId);
            if (userOpt.isEmpty()) {
                HttpUtil.redirect(exchange, "/login");
                return;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("username", userOpt.get().getUsername());
            model.put("entries", userBookDao.findAllByUserId(userId));
            HttpUtil.sendHtml(exchange, 200, TemplateEngine.render("home.ftl", model));
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
        }
    }
}
