package com.seproject.seboard.domain.model.post;

import com.seproject.seboard.domain.model.category.Category;
import com.seproject.seboard.domain.model.category.Menu;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.common.FileMetaData;
import com.seproject.seboard.domain.model.common.ReportThreshold;
import com.seproject.seboard.domain.model.common.Status;
import com.seproject.seboard.domain.model.post.exposeOptions.ExposeOption;
import com.seproject.seboard.domain.model.post.exposeOptions.ExposeState;
import com.seproject.seboard.domain.model.post.exposeOptions.Privacy;
import com.seproject.seboard.domain.model.user.Anonymous;
import com.seproject.seboard.domain.model.user.BoardUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
@Table(name = "posts")
public class Post {
    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long postId;
//    @Range(min = 1, max = 100)
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
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "expose_option_id")
    private ExposeOption exposeOption;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private Set<FileMetaData> attachments = new HashSet<>();
    private int anonymousCount;
    private int reportCount;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NORMAL;
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
        this.title = title;
    }

    public void changeContents(String contents) {
        this.contents = contents;
    }

    public void changeExposeOption(ExposeState exposeState, String password) {
        if(exposeState==ExposeState.PRIVACY &&
                exposeOption.getExposeState()==ExposeState.PRIVACY && password==null){
            return;
        }else if(exposeOption.getExposeState()==exposeState){
            return;
        }else{
            exposeOption = ExposeOption.of(exposeState, password);
        }

    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changePin(boolean pinState) {
        this.pined = pinState;
    }

    public void addAttachment(FileMetaData attachment) {
        attachments.add(attachment);
    }
    public void removeAttachment(FileMetaData attachment){
        attachments.remove(attachment);
    }

    public Set<FileMetaData> getAttachments() {
        return new HashSet<>(attachments);
    }

    public boolean hasAttachments(){
        return !attachments.isEmpty();
    }

    public void delete(boolean isPermanent) {
        if(isPermanent) {
            this.status = Status.PERMANENT_DELETED;
        }else{
            this.status = Status.TEMP_DELETED;
        }
    }

    public void increaseViews() {
        this.views++;
    }
    public void increaseReportCount(ReportThreshold reportThreshold){
        this.reportCount++;
        if(reportThreshold.isOverThreshold(reportCount)){
            this.status = Status.REPORTED;
        }
    }

    public boolean checkPassword(String password) {
        if(exposeOption.getExposeState()==ExposeState.PRIVACY){
            return ((Privacy)exposeOption).checkPassword(password);
        }else{
            return false;
        }
    }

    public void restore() {
        if(status!=Status.NORMAL){
            status = Status.NORMAL;
            reportCount = 0;
        }
    }
}
