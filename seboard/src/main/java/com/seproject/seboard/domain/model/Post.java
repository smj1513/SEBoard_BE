package com.seproject.seboard.domain.model;

import com.seproject.seboard.domain.model.author.Author;
import com.seproject.seboard.domain.model.author.Member;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class Post {
    private Long postId;
    private Long categoryId;
    private String title;
    private String contents;
    private int views;
    private Author author;

    public boolean isSameAuthor(Author author){
        return this.author.equals(author);
    }
    public void update(String title,String contents,Long categoryId) {
        this.title = title;
        this.contents = contents;
        this.categoryId = categoryId;
    }
}
