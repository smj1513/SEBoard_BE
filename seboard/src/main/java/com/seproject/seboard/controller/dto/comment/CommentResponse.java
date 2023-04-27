package com.seproject.seboard.controller.dto.comment;

import com.seproject.seboard.controller.dto.PaginationResponse;
import com.seproject.seboard.controller.dto.user.UserResponse;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentResponse {
    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class CommentListResponse{
        private PaginationResponse paginationInfo;
        private List<CommentListElement> data;

        public static CommentListResponse toDto(List<CommentListElement> data, PaginationResponse paginationInfo){
            return builder()
                    .data(data)
                    .paginationInfo(paginationInfo)
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class CommentListElement{
        private Long commentId;
        private UserResponse author;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String contents;
        private boolean isEditable;
        private List<ReplyResponse> subComments;

        public static CommentListElement toDto(Comment comment, boolean isEditable, List<ReplyResponse> subComments){
//            UserResponse userResponse = UserResponse.toDTO(comment.getAuthor());


            return builder()
                    .commentId(comment.getCommentId())
//                    .author(userResponse)
                    .createdAt(comment.getBaseTime().getCreatedAt())
                    .modifiedAt(comment.getBaseTime().getModifiedAt())
                    .contents(comment.getContents())
                    .isEditable(isEditable)
                    .subComments(subComments)
                    .build();
        }
    }
}
