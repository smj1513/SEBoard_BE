package com.seproject.seboard.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        private List<CommentListElement> content;

        public static CommentListResponse toDto(List<CommentListElement> data, PaginationResponse paginationInfo){
            return builder()
                    .content(data)
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
        @JsonProperty("isEditable")
        private boolean isEditable;
        @JsonProperty("isActive")
        private boolean isActive;
        @JsonProperty("isReadOnlyAuthor")
        private boolean isReadOnlyAuthor;
        private List<ReplyResponse> subComments;

        public static CommentListElement toDto(Comment comment, boolean isEditable, List<ReplyResponse> subComments){
            UserResponse userResponse = null;
            String contents = null;
            //TODO : dto에 로직이 들어가는게 맞나?
            if(comment.isDeleted()){
                contents = "삭제된 댓글입니다.";
                userResponse = new UserResponse(null, "(알수 없음)");
            }else{
                contents = comment.getContents();
                userResponse = new UserResponse(comment.getAuthor());
            }

            return builder()
                    .commentId(comment.getCommentId())
                    .author(userResponse)
                    .createdAt(comment.getBaseTime().getCreatedAt())
                    .modifiedAt(comment.getBaseTime().getModifiedAt())
                    .contents(contents)
                    .isEditable(isEditable)
                    .isActive(comment.isDeleted()) //TODO : 작성자만 읽을 수 있는 경우 추가 필요
                    .isReadOnlyAuthor(comment.isOnlyReadByAuthor())
                    .subComments(subComments)
                    .build();
        }
    }
}
