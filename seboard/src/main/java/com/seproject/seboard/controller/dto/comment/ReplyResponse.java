package com.seproject.seboard.controller.dto.comment;

import com.seproject.seboard.controller.dto.user.UserResponse;
import com.seproject.seboard.domain.model.comment.Reply;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class ReplyResponse {
    private Long commentId;
    private Long tag;
    private UserResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String contents;
    private boolean isEditable;

    public static ReplyResponse toDto(Reply reply, boolean isEditable){
//        UserResponse userResponse = UserResponse.toDTO(reply.getAuthor());

        return builder()
                .commentId(reply.getCommentId())
                .tag(reply.getTag().getCommentId())
//                .author(userResponse)
                .createdAt(reply.getBaseTime().getCreatedAt())
                .modifiedAt(reply.getBaseTime().getModifiedAt())
                .contents(reply.getContents())
                .isEditable(isEditable)
                .build();
    }
}
