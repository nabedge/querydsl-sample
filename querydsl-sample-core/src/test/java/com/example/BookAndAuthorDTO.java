package com.example;

import com.example.db.querydsl.gen.Book;

public class BookAndAuthorDTO extends Book {
    
    private static final long serialVersionUID = 1L;
    
    private String authorName;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
