package com.seproject.seboard.domain.model.comment;

import com.seproject.seboard.domain.model.BaseTime;
import com.seproject.seboard.domain.model.Post;
import com.seproject.seboard.domain.model.exposeOptions.ExposeOption;
import com.seproject.seboard.domain.model.user.User;
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
    @JoinColumn(name = "author_id")
    private User author;

    @OneToOne
    @JoinColumn(name="expose_option_id")
    private ExposeOption exposeOption;

    public boolean isNamed() {
        return !author.isAnonymous();
    }

    public boolean isWrittenBy(User user) {
        return user.equals(this.author);
    }

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
