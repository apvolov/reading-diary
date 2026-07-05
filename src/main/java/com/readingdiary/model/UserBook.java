package com.readingdiary.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserBook {
    private Long id;
    private Long userId;
    private String title;
    private String author;
    private Integer year;
    private String description;
    private ReadingStatus status;
    private Integer rating;
    private String review;
    private LocalDateTime dateAdded;
    private LocalDate dateFinished;

    public UserBook() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ReadingStatus getStatus() { return status; }
    public void setStatus(ReadingStatus status) { this.status = status; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public LocalDateTime getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDateTime dateAdded) { this.dateAdded = dateAdded; }

    public LocalDate getDateFinished() { return dateFinished; }
    public void setDateFinished(LocalDate dateFinished) { this.dateFinished = dateFinished; }
}
