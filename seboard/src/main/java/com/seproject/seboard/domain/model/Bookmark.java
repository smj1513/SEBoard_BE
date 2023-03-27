package com.seproject.seboard.domain.model;

import com.seproject.seboard.domain.model.user.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
@Table(name = "bookmarks")
public class Bookmark {
    @Id @GeneratedValue
    private Long bookmarkId;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post markedPost;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}