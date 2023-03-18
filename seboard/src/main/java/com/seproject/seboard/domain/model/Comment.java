package com.seproject.seboard.domain.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Comment implements Comparable<Comment> {
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

    public void change(String contents) {
        this.contents = contents;
    }

    public boolean isWrittenBy(Author author) {
        return author.equals(this.author);
    }


    @Override
    public int compareTo(Comment o) {

        Comment thisTarget = this;
        Comment opposite = o;

        boolean thisIsReply = isReply();
        boolean targetIsReply = o.isReply();

        if(thisIsReply) {
            thisTarget = this.superComment;
        }

        if(targetIsReply) {
            opposite = o.superComment;
        }

        if (thisIsReply && targetIsReply && thisTarget.equals(opposite)) {
            return baseTime.getCreatedAt().compareTo(opposite.getBaseTime().getCreatedAt());
        }

        return thisTarget.baseTime.getCreatedAt().compareTo(opposite.getBaseTime().getCreatedAt());
    }
}
