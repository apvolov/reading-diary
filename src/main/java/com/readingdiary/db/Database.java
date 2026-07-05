package com.readingdiary.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL =
            System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/reading_diary");
    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "postgres");
    private static final String PASSWORD =
            System.getenv().getOrDefault("DB_PASSWORD", "postgres");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("PostgreSQL JDBC-драйвер не найден в classpath: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
