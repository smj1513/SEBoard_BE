package com.seproject.seboard.domain.model.comment;

import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.exposeOptions.ExposeOption;
import com.seproject.seboard.domain.model.user.BoardUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@SuperBuilder
@AllArgsConstructor
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

    @OneToOne
    @JoinColumn(name="expose_option_id")
    private ExposeOption exposeOption;

    public Comment(Long commentId, String contents, boolean isDeleted,
                   BaseTime baseTime, Post post, BoardUser author, ExposeOption exposeOption) {

        if(!isValidateContents(contents)) {
            throw new IllegalArgumentException();
        }

        this.commentId = commentId;
        this.contents = contents;
        this.isDeleted = isDeleted;
        this.baseTime = baseTime;
        this.post = post;
        this.author = author;
        this.exposeOption = exposeOption;
    }

    public Reply writeReply(String contents, Comment taggedComment, BoardUser author) {
        return Reply.builder()
                .contents(contents)
                .author(author)
                .tag(taggedComment)
                .baseTime(BaseTime.now())
                .superComment(this)
                .build();
    }

    public boolean isWrittenBy(Long accountId) {
        return author.isOwnAccountId(accountId);
    }

    public void changeContents(String contents) {
        if(!isValidateContents(contents)){
            throw new IllegalArgumentException();
        }

        this.contents = contents;
    }

    private boolean isValidateContents(String contents) {
        return CONTENTS_MIN_SIZE < contents.length() && contents.length() <= CONTENTS_MAX_SIZE;
    }

    public void delete() {
        this.isDeleted = true;
    }
}

//    public boolean isNamed() {
//        return !author.isAnonymous();
//    }
//
//    public boolean isWrittenBy(User user) {
//        return user.equals(this.author);
//    }

//    @Override
//    public int compareTo(Comment o) {
//
//        Comment thisTarget = this;
//        Comment opposite = o;
//
//        boolean thisIsReply = isReply();
//        boolean targetIsReply = o.isReply();
//
//        if(thisIsReply) {
//            thisTarget = this.superComment;
//        }
//
//        if(targetIsReply) {
//            opposite = o.superComment;
//        }
//
//        if (thisIsReply && targetIsReply && thisTarget.equals(opposite)) {
//            return baseTime.getCreatedAt().compareTo(opposite.getBaseTime().getCreatedAt());
//        }
//
//        return thisTarget.baseTime.getCreatedAt().compareTo(opposite.getBaseTime().getCreatedAt());
//    }
}
