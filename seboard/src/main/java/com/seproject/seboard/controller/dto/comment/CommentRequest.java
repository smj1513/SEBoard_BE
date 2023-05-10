package com.seproject.seboard.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentEditCommand;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentWriteCommand;
import lombok.Data;

import javax.validation.constraints.NotNull;

public class CommentRequest {

    @Data
    public static class CreateCommentRequest {
        @NotNull
        private Long postId;
        @NotNull
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
        private String contents;
        @JsonProperty("isReadOnlyAuthor")
        private boolean isReadOnlyAuthor;
        public CommentEditCommand toCommand(Long commentId, Long accountId) {
            return CommentEditCommand.builder()
                    .accountId(accountId)
                    .commentId(commentId)
                    .contents(contents)
                    .isOnlyReadByAuthor(isReadOnlyAuthor)
                    .build();
        }
    }


}
