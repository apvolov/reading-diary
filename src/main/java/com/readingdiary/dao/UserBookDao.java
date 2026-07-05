package com.readingdiary.dao;

import com.readingdiary.db.Database;
import com.readingdiary.model.ReadingStatus;
import com.readingdiary.model.UserBook;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookDao {

    public UserBook add(UserBook entry) throws SQLException {
        String sql = """
                INSERT INTO user_books (user_id, title, author, year, description)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, entry.getUserId());
            stmt.setString(2, entry.getTitle());
            stmt.setString(3, entry.getAuthor());
            stmt.setObject(4, entry.getYear());
            stmt.setString(5, entry.getDescription());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    entry.setId(keys.getLong(1));
                }
            }
            entry.setStatus(ReadingStatus.PLANNED);
            return entry;
        }
    }

    public List<UserBook> findAllByUserId(long userId) throws SQLException {
        String sql = """
                SELECT id, user_id, title, author, year, description,
                       status, rating, review, date_added, date_finished
                FROM user_books
                WHERE user_id = ?
                ORDER BY date_added DESC
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<UserBook> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
                return result;
            }
        }
    }

    public Optional<UserBook> findById(long id, long userId) throws SQLException {
        String sql = """
                SELECT id, user_id, title, author, year, description,
                       status, rating, review, date_added, date_finished
                FROM user_books
                WHERE id = ? AND user_id = ?
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setLong(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        }
    }

    public void updateStatus(long id, long userId, ReadingStatus status) throws SQLException {
        String sql = "UPDATE user_books SET status = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setLong(2, id);
            stmt.setLong(3, userId);
            stmt.executeUpdate();
        }
    }

    public void updateDetails(long id, long userId, Integer rating, String review, LocalDate dateFinished) throws SQLException {
        String sql = """
                UPDATE user_books
                SET rating = ?, review = ?, date_finished = ?
                WHERE id = ? AND user_id = ?
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, rating);
            stmt.setString(2, review != null && review.isBlank() ? null : review);
            stmt.setObject(3, dateFinished != null ? Date.valueOf(dateFinished) : null);
            stmt.setLong(4, id);
            stmt.setLong(5, userId);
            stmt.executeUpdate();
        }
    }

    private UserBook mapRow(ResultSet rs) throws SQLException {
        UserBook entry = new UserBook();
        entry.setId(rs.getLong("id"));
        entry.setUserId(rs.getLong("user_id"));
        entry.setTitle(rs.getString("title"));
        entry.setAuthor(rs.getString("author"));
        entry.setYear((Integer) rs.getObject("year"));
        entry.setDescription(rs.getString("description"));
        entry.setStatus(ReadingStatus.valueOf(rs.getString("status")));
        entry.setRating((Integer) rs.getObject("rating"));
        entry.setReview(rs.getString("review"));
        entry.setDateAdded(rs.getTimestamp("date_added").toLocalDateTime());

        Date finished = rs.getDate("date_finished");
        if (finished != null) {
            entry.setDateFinished(finished.toLocalDate());
        }
        return entry;
    }
}
