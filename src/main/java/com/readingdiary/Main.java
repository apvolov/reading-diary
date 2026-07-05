package com.readingdiary;

import com.readingdiary.handler.AddBookHandler;
import com.readingdiary.handler.EditEntryHandler;
import com.readingdiary.handler.HomeHandler;
import com.readingdiary.handler.LoginHandler;
import com.readingdiary.handler.LogoutHandler;
import com.readingdiary.handler.RegisterHandler;
import com.readingdiary.handler.StaticFileHandler;
import com.readingdiary.handler.UpdateStatusHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/static", new StaticFileHandler());
        server.createContext("/books/add", new AddBookHandler());
        server.createContext("/diary/status", new UpdateStatusHandler());
        server.createContext("/diary/edit", new EditEntryHandler());
        server.createContext("/", new HomeHandler());
        server.createContext("/register", new RegisterHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/logout", new LogoutHandler());

        server.setExecutor(Executors.newFixedThreadPool(10));

        server.start();
        System.out.println("Сервер запущен: http://localhost:" + port);
    }
}
