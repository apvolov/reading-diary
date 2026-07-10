package com.readingdiary.handler;

import com.readingdiary.dao.ShelfDao;
import com.readingdiary.model.Shelf;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.SessionManager;
import com.readingdiary.util.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AddShelfHandler implements HttpHandler {

    private final ShelfDao shelfDao = new ShelfDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> cookies = HttpUtil.parseCookies(exchange);
        Long userId = SessionManager.getUserId(cookies.get("session_id"));

        if (userId == null) {
            HttpUtil.redirect(exchange, "/login");
            return;
        }

        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            HttpUtil.sendHtml(exchange, 200, TemplateEngine.render("add-shelf.ftl", Map.of()));
            return;
        }

        if (method.equals("POST")) {
            handlePost(exchange, userId);
            return;
        }

        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

    private void handlePost(HttpExchange exchange, long userId) throws IOException {
        Map<String, String> form = HttpUtil.parseFormBody(exchange);
        String name = form.get("name");

        if (name == null || name.isBlank()) {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Название полки обязательно");
            model.put("form", form);
            HttpUtil.sendHtml(exchange, 400, TemplateEngine.render("add-shelf.ftl", model));
            return;
        }

        try {
            Shelf shelf = new Shelf();
            shelf.setUserId(userId);
            shelf.setName(name.strip());

            shelfDao.add(shelf);
            HttpUtil.redirect(exchange, "/");
        } catch (SQLException e) {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Полка с таким названием уже существует");
            model.put("form", form);
            HttpUtil.sendHtml(exchange, 400, TemplateEngine.render("add-shelf.ftl", model));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Ошибка сервера, попробуйте позже");
            HttpUtil.sendHtml(exchange, 500, TemplateEngine.render("add-shelf.ftl", model));
        }
    }
}
