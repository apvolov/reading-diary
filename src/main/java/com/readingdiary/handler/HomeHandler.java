package com.readingdiary.handler;

import com.readingdiary.dao.UserDao;
import com.readingdiary.model.User;
import com.readingdiary.util.HtmlTemplates;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.SessionManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class HomeHandler implements HttpHandler {

    private final UserDao userDao = new UserDao();

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

            String html = HtmlTemplates.page("Личный кабинет", """
                    <h1>Привет, %s!</h1>
                    <p>Пустой читательский дневник.</p>
                    <form method="POST" action="/logout">
                        <button type="submit">Выйти</button>
                    </form>
                    """.formatted(userOpt.get().getUsername()));

            HttpUtil.sendHtml(exchange, 200, html);
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
        }
    }
}
