package com.seproject.seboard.domain.model.post;

import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.common.FileMetaData;
import com.seproject.seboard.domain.model.exposeOptions.ExposeOption;
import com.seproject.seboard.domain.model.exposeOptions.ExposeState;
import com.seproject.seboard.domain.model.exposeOptions.Public;
import com.seproject.seboard.domain.model.user.Anonymous;
import com.seproject.seboard.domain.model.user.BoardUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Id @GeneratedValue
    @Column(name = "post_id")
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
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "expose_option_id")
    @Builder.Default
    private ExposeOption exposeOption = new Public();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private Set<FileMetaData> attachments = new HashSet<>();
    private int anonymousCount;

    public Post(Long postId, String title, String contents, int views,
                boolean pined, BaseTime baseTime, Category category,
                BoardUser author, ExposeOption exposeOption,  Set<FileMetaData> attachments,  int anonymousCount) {
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
        this.anonymousCount = anonymousCount;
    }

    public boolean isNamed() {
        return !author.isAnonymous();
    }

    public boolean isWrittenBy(Long accountId) {
        return author.isOwnAccountId(accountId);
    }

    public Anonymous createAnonymous(Long accountId) {
        return Anonymous.builder()
                .name(String.format("익명%d", ++anonymousCount))
                .accountId(accountId)
                .build();
    }

    public Comment writeComment(String contents, BoardUser author, boolean isOnlyReadByAuthor){
        //TODO : post가 kumoh이면 comment는 public 어차피 안되잖아? 그거 고려해야되나?
        return Comment.builder()
                .contents(contents)
                .baseTime(BaseTime.now())
                .post(this)
                .author(author)
                .isOnlyReadByAuthor(isOnlyReadByAuthor)
                .build();
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

    public void changeExposeOption(ExposeState exposeState, String password) {
        if(exposeState==ExposeState.PRIVACY &&
                exposeOption.getExposeState()==ExposeState.PRIVACY && password==null){
            return;
        }

        exposeOption = ExposeOption.of(exposeState, password);
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changePin(boolean pinState) {
        this.pined = pinState;
    }

    private boolean isValidTitle(String title) {
        return TITLE_MIN_SIZE < title.length() && title.length() <= TITLE_MAX_SIZE;
    }

    private boolean isValidContents(String contents) {
        return CONTENTS_MIN_SIZE < contents.length() && contents.length() <= CONTENTS_MAX_SIZE;
    }

    public void removeAttachment(FileMetaData attachment){
        attachments.remove(attachment);
    }

    public Set<FileMetaData> getAttachments() {
        return new HashSet<>(attachments);
    }

    public void addAttachment(FileMetaData attachment) {
        attachments.add(attachment);
    }
}
