package com.readingdiary.handler;

import com.readingdiary.dao.UserBookDao;
import com.readingdiary.util.CoverStorage;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.MultipartParser;
import com.readingdiary.util.SessionManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class DeleteEntryHandler implements HttpHandler {

    private final UserBookDao userBookDao = new UserBookDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> cookies = HttpUtil.parseCookies(exchange);
        Long userId = SessionManager.getUserId(cookies.get("session_id"));

        if (userId == null) {
            HttpUtil.redirect(exchange, "/login");
            return;
        }

        if (!exchange.getRequestMethod().equals("POST")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        try {
            MultipartParser form = MultipartParser.parse(exchange);
            long id = Long.parseLong(form.getField("id"));

            if (userBookDao.findById(id, userId).isEmpty()) {
                HttpUtil.redirect(exchange, "/");
                return;
            }

            userBookDao.deleteById(id, userId);
            CoverStorage.delete(id);

            HttpUtil.redirect(exchange, "/");
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtil.redirect(exchange, "/");
        }
    }
}