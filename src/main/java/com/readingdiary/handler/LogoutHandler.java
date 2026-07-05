package com.readingdiary.handler;

import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.SessionManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class LogoutHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> cookies = HttpUtil.parseCookies(exchange);
        SessionManager.invalidate(cookies.get("session_id"));
        HttpUtil.clearSessionCookie(exchange);
        HttpUtil.redirect(exchange, "/login");
    }
}
