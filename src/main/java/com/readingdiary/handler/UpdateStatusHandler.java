package com.readingdiary.handler;

import com.readingdiary.dao.UserBookDao;
import com.readingdiary.model.ReadingStatus;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.SessionManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class UpdateStatusHandler implements HttpHandler {

    private final UserBookDao userBookDao = new UserBookDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        Map<String, String> cookies = HttpUtil.parseCookies(exchange);
        Long userId = SessionManager.getUserId(cookies.get("session_id"));

        if (userId == null) {
            HttpUtil.redirect(exchange, "/login");
            return;
        }

        try {
            Map<String, String> form = HttpUtil.parseFormBody(exchange);
            long id = Long.parseLong(form.get("id"));
            ReadingStatus status = ReadingStatus.valueOf(form.get("status"));

            userBookDao.updateStatus(id, userId, status);
            HttpUtil.redirect(exchange, "/");
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtil.redirect(exchange, "/");
        }
    }
}
