package com.seproject.seboard.domain.model.post;

import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.exposeOptions.ExposeOption;
import com.seproject.seboard.domain.model.user.BoardUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Builder
@Table(name = "posts")
public class Post {
    private static final Integer TITLE_MAX_SIZE = 50;
    private static final Integer TITLE_MIN_SIZE = 4;
    private static final Integer CONTENTS_MAX_SIZE = 1000;
    private static final Integer CONTENTS_MIN_SIZE = 10;
    @Id
    @GeneratedValue
    private Long postId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String contents;
    private int views;
    private boolean pined; //TODO: pined 검증 로직 필요
    private BaseTime baseTime;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "board_user_id")
    private BoardUser author;
    @OneToOne
    @JoinColumn(name = "expose_option_id")
    private ExposeOption exposeOption;
    @OneToMany
    private List<Attachment> attachments;


    public Post(Long postId, String title, String contents, int views,
                boolean pined, BaseTime baseTime, Category category,
                BoardUser author, ExposeOption exposeOption, List<Attachment> attachments) {
        if(!isValidTitle(title)) {
            throw new IllegalArgumentException();
        }

        if(!isValidContents(contents)) {
            throw new IllegalArgumentException();
        }

        this.postId = postId;
        this.title = title;
        this.contents = contents;
        this.views = views;
        this.pined = pined;
        this.baseTime = baseTime;
        this.category = category;
        this.author = author;
        this.exposeOption = exposeOption;
        this.attachments = attachments;
    }

    public boolean isNamed() {
        return !author.isAnonymous();
    }

    public boolean isWrittenBy(Long accountId) {
        return author.isOwnAccountId(accountId);
    }

    public void changeTitle(String title) {
        if(!isValidTitle(title)) {
            throw new IllegalArgumentException();
        }

        this.title = title;
    }

    public void changeContents(String contents) {
        if(!isValidContents(contents)) {
            throw new IllegalArgumentException();
        }

        this.contents = contents;
    }

    public void changePin(boolean pinState) {
        this.pined = pinState;
    }

    private boolean isValidTitle(String title) {
        return TITLE_MIN_SIZE < title.length() && title.length() <= TITLE_MAX_SIZE;
    }

    public boolean isValidContents(String contents) {
        return CONTENTS_MIN_SIZE < contents.length() && contents.length() <= CONTENTS_MAX_SIZE;
    }

//
//    public boolean isNamed() {
//        return !author.isAnonymous();
//    }
}
