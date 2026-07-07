package com.readingdiary.handler;

import com.readingdiary.dao.UserBookDao;
import com.readingdiary.model.ReadingStatus;
import com.readingdiary.model.UserBook;
import com.readingdiary.util.CoverStorage;
import com.readingdiary.util.HttpUtil;
import com.readingdiary.util.MultipartParser;
import com.readingdiary.util.SessionManager;
import com.readingdiary.util.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EditEntryHandler implements HttpHandler {

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
            handleGet(exchange, userId);
            return;
        }

        if (method.equals("POST")) {
            handlePost(exchange, userId);
            return;
        }

        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }

    private void handleGet(HttpExchange exchange, long userId) throws IOException {
        Map<String, String> params = HttpUtil.parseQueryParams(exchange);
        String idStr = params.get("id");

        if (idStr == null) {
            HttpUtil.redirect(exchange, "/");
            return;
        }

        try {
            long id = Long.parseLong(idStr);
            Optional<UserBook> entryOpt = userBookDao.findById(id, userId);

            if (entryOpt.isEmpty()) {
                HttpUtil.redirect(exchange, "/");
                return;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("entry", entryOpt.get());
            HttpUtil.sendHtml(exchange, 200, TemplateEngine.render("edit-entry.ftl", model));
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtil.redirect(exchange, "/");
        }
    }

    private void handlePost(HttpExchange exchange, long userId) throws IOException {
        long id = -1;
        try {
            MultipartParser form = MultipartParser.parse(exchange);
            id = Long.parseLong(form.getField("id"));

            if (userBookDao.findById(id, userId).isEmpty()) {
                HttpUtil.redirect(exchange, "/");
                return;
            }

            ReadingStatus status = ReadingStatus.valueOf(form.getField("status"));
            userBookDao.updateStatus(id, userId, status);

            String ratingStr = form.getField("rating");
            Integer rating = (ratingStr != null && !ratingStr.isBlank()) ? Integer.parseInt(ratingStr) : null;

            String review = form.getField("review");

            String dateStr = form.getField("date_finished");
            LocalDate dateFinished = (dateStr != null && !dateStr.isBlank()) ? LocalDate.parse(dateStr) : null;

            userBookDao.updateDetails(id, userId, rating, review, dateFinished);

            MultipartParser.UploadedFile file = form.getUploadedFile();
            if (file != null && file.data.length > 0) {
                String filename = CoverStorage.save(id, file.originalFilename, file.data);
                userBookDao.updateCover(id, userId, filename);
            }

            HttpUtil.redirect(exchange, "/diary/edit?id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtil.redirect(exchange, id >= 0 ? "/diary/edit?id=" + id : "/");
        }
    }
}
