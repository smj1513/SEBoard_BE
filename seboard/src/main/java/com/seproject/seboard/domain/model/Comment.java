package com.seproject.seboard.domain.model;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class Comment {
    private Long commentId;
    private Post post;
    private Comment superComment;
    private Comment tag;
    private Author author;
    private String contents;
    private BaseTime baseTime;
    private boolean isDeleted;

    public boolean isNamed(){
        return true;
    }

    public boolean isReply(){
        return superComment != null;
    }
}
