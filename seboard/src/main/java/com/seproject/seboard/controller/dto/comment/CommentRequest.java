package com.seproject.seboard.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentEditCommand;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentWriteCommand;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentRequest {

    @Data
    public static class CreateCommentRequest {
        @NotNull
        private Long postId;
        @NotBlank
        private String contents;
        @NotNull
        @JsonProperty("isAnonymous")
        private boolean isAnonymous;
        @NotNull
        @JsonProperty("isReadOnlyAuthor")
        private boolean isReadOnlyAuthor;

        public CommentWriteCommand toCommand(String loginId) {
            return CommentWriteCommand.builder()
                    .postId(postId)
                    .loginId(loginId)
                    .contents(contents)
                    .isAnonymous(isAnonymous)
                    .isOnlyReadByAuthor(isReadOnlyAuthor)
                    .build();
        }
    }

    @Data
    public static class UpdateCommentRequest {
        @NotNull
        private String contents;
        @NotNull
        @JsonProperty("isReadOnlyAuthor")
        private boolean isReadOnlyAuthor;
        public CommentEditCommand toCommand(Long commentId, String loginId) {
            return CommentEditCommand.builder()
                    .commentId(commentId)
                    .loginId(loginId)
                    .contents(contents)
                    .isOnlyReadByAuthor(isReadOnlyAuthor)
                    .build();
        }
    }


}
