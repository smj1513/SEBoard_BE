package com.seproject.seboard.domain.model.comment;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "replies")
@SuperBuilder
public class Reply extends Comment {

    @JoinColumn(name = "super_comment_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Comment superComment;

    @JoinColumn(name = "tag_comment_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Comment tag;

}
