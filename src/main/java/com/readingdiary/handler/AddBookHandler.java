package com.readingdiary.handler;

import com.readingdiary.dao.UserBookDao;
import com.readingdiary.model.UserBook;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.SessionManager;
import com.readingdiary.util.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddBookHandler implements HttpHandler {

    private final UserBookDao userBookDao = new UserBookDao();

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
            HttpUtil.sendHtml(exchange, 200, TemplateEngine.render("add-book.ftl", Map.of()));
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
        String title = form.get("title");
        String author = form.get("author");
        String yearStr = form.get("year");
        String description = form.get("description");

        if (title == null || title.isBlank() || author == null || author.isBlank()) {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Название и автор обязательны");
            model.put("form", form);
            HttpUtil.sendHtml(exchange, 400, TemplateEngine.render("add-book.ftl", model));
            return;
        }

        try {
            Integer year = null;
            if (yearStr != null && !yearStr.isBlank()) {
                year = Integer.parseInt(yearStr);
            }

            UserBook entry = new UserBook();
            entry.setUserId(userId);
            entry.setTitle(title.strip());
            entry.setAuthor(author.strip());
            entry.setYear(year);
            entry.setDescription(description != null && !description.isBlank() ? description.strip() : null);

            userBookDao.add(entry);
            HttpUtil.redirect(exchange, "/");
        } catch (NumberFormatException e) {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Год должен быть числом");
            model.put("form", form);
            HttpUtil.sendHtml(exchange, 400, TemplateEngine.render("add-book.ftl", model));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Ошибка сервера, попробуйте позже");
            HttpUtil.sendHtml(exchange, 500, TemplateEngine.render("add-book.ftl", model));
        }
    }
}
