package com.readingdiary.handler;

import com.readingdiary.dao.ShelfDao;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.SessionManager;
import com.readingdiary.util.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShelvesHandler implements HttpHandler {

    private final ShelfDao shelfDao = new ShelfDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> cookies = HttpUtil.parseCookies(exchange);
        Long userId = SessionManager.getUserId(cookies.get("session_id"));

        if (userId == null) {
            HttpUtil.redirect(exchange, "/login");
            return;
        }

        try {
            Map<String, Object> model = new HashMap<>();
            model.put("shelves", shelfDao.findAllByUserId(userId));
            HttpUtil.sendHtml(exchange, 200, TemplateEngine.render("shelves.ftl", model));
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
        }
    }
}
