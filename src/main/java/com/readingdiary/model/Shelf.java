package com.readingdiary.model;

import java.util.ArrayList;
import java.util.List;

public class Shelf {
    private Long id;
    private Long userId;
    private String name;
    private int bookCount;
    private List<UserBook> books = new ArrayList<>();

    public Shelf() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public List<UserBook> getBooks() {
        return books;
    }

    public void setBooks(List<UserBook> books) {
        this.books = books;
    }
}
