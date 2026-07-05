package com.readingdiary.model;

public class Book {
    private Long id;
    private String title;
    private String author;
    private Integer year;
    private String description;

    public Book() {
    }

    public Book(Long id, String title, String author, Integer year, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
