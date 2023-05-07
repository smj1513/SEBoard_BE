package com.seproject.seboard.domain.model.comment;

import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.BoardUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "comments")
public class Comment {
    private static final int CONTENTS_MAX_SIZE = 1000;
    private static final int CONTENTS_MIN_SIZE = 10;

    @Id @GeneratedValue
    private Long commentId;

    @Column(columnDefinition = "TEXT")
    private String contents;

    private boolean isDeleted;
    @Embedded
    private BaseTime baseTime;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @OneToOne
    @JoinColumn(name = "board_user_id")
    private BoardUser author;
    private boolean isOnlyReadByAuthor;


    private Comment(Long commentId, String contents, boolean isDeleted,
                   BaseTime baseTime, Post post, BoardUser author, boolean isOnlyReadByAuthor) {

        if(!isValidContents(contents)) {
            throw new IllegalArgumentException();
        }

        this.commentId = commentId;
        this.contents = contents;
        this.isDeleted = isDeleted;
        this.baseTime = baseTime;
        this.post = post;
        this.author = author;
        this.isOnlyReadByAuthor = isOnlyReadByAuthor;
    }

    public Reply writeReply(String contents, Comment taggedComment, BoardUser author, boolean isOnlyReadByAuthor){
        return Reply.builder()
                .post(post)
                .contents(contents)
                .author(author)
                .tag(taggedComment)
                .baseTime(BaseTime.now())
                .isOnlyReadByAuthor(isOnlyReadByAuthor)
                .superComment(this)
                .build();
    }

    public boolean isWrittenBy(Long accountId) {
        return author.isOwnAccountId(accountId);
    }

    public void changeContents(String contents) {
        if(!isValidContents(contents)){
            throw new IllegalArgumentException();
        }

        this.contents = contents;
    }

    public void changeOnlyReadByAuthor(boolean isOnlyReadByAuthor) {
        this.isOnlyReadByAuthor = isOnlyReadByAuthor;
    }


    private boolean isValidContents(String contents) {
        return CONTENTS_MIN_SIZE < contents.length() && contents.length() <= CONTENTS_MAX_SIZE;
    }

    public void delete() {
        this.isDeleted = true;
    }

}
