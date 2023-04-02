package com.seproject.seboard.domain.model.post;

import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.exposeOptions.ExposeOption;
import com.seproject.seboard.domain.model.user.BoardUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@SuperBuilder
@Table(name = "posts")
public class Post {
    @Id @GeneratedValue
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

//    public boolean isWrittenBy(User user) {
//        return user.equals(author);
//    }
//
//    public boolean isNamed() {
//        return !author.isAnonymous();
//    }
//    public void pin() {
//        if(!isPined()) {
//            this.pined = true;
//        }
//    }
//
//    public void unPin() {
//        if(isPined()) {
//            this.pined = false;
//        }
//    }
}
