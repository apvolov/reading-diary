package com.readingdiary.dao;

import com.readingdiary.db.Database;
import com.readingdiary.model.Shelf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShelfDao {

    public Shelf add(Shelf shelf) throws SQLException {
        String sql = "INSERT INTO shelves (user_id, name) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, shelf.getUserId());
            stmt.setString(2, shelf.getName());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    shelf.setId(keys.getLong(1));
                }
            }
            return shelf;
        }
    }

    public List<Shelf> findAllByUserId(long userId) throws SQLException {
        String sql = """
                SELECT s.id, s.user_id, s.name, COUNT(sb.user_book_id) AS book_count
                FROM shelves s
                LEFT JOIN shelf_books sb ON sb.shelf_id = s.id
                WHERE s.user_id = ?
                GROUP BY s.id, s.user_id, s.name
                ORDER BY s.name
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Shelf> result = new ArrayList<>();
                while (rs.next()) {
                    Shelf shelf = new Shelf();
                    shelf.setId(rs.getLong("id"));
                    shelf.setUserId(rs.getLong("user_id"));
                    shelf.setName(rs.getString("name"));
                    shelf.setBookCount(rs.getInt("book_count"));
                    result.add(shelf);
                }
                return result;
            }
        }
    }
}
