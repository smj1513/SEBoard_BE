package com.seproject.seboard.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Post {
    private final Long postId;
    private Category category;
    private BaseTime baseTime;
    private String title;
    private String contents;
    private int views;
    private Author author;
    private boolean pined; //TODO: pined 검증 로직 필요

    public void update(String title,String contents,Category category) {
        this.title = title;
        this.contents = contents;
        this.category = category;
    }

    public boolean isWrittenBy(Author author) {
        return author.equals(this.author);
    }

    public boolean isNamedPost(){
        return true;
    }

    public void pin() {
        if(!isPined()) {
            this.pined = true;
        }
    }

    public void unPin() {
        if(isPined()) {
            this.pined = false;
        }
    }
}
