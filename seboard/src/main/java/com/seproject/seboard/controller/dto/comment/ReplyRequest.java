package com.seproject.seboard.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

import static com.seproject.seboard.application.dto.comment.ReplyCommand.*;

public class ReplyRequest {

    @Data
    public static class CreateReplyRequest {
        @NotNull
        private Long postId;
        @NotNull
        private Long superCommentId;
        @NotNull
        private Long tagCommentId;
        @NotNull
        @JsonProperty("isAnonymous")
        private boolean isAnonymous;
        @NotNull
        private String contents;
        @NotNull
        @JsonProperty("isReadOnlyAuthor")
        private boolean isReadOnlyAuthor;

        public ReplyWriteCommand toCommand(String loginId) {
            return ReplyWriteCommand.builder()
                    .postId(postId)
                    .loginId(loginId)
                    .superCommentId(superCommentId)
                    .tagCommentId(tagCommentId)
                    .contents(contents)
                    .isAnonymous(isAnonymous)
                    .isOnlyReadByAuthor(isReadOnlyAuthor)
                    .build();
        }
    }

    @Data
    public static class UpdateReplyRequest {
        private String contents;
        @JsonProperty("isReadOnlyAuthor")
        private boolean isReadOnlyAuthor;

        public ReplyEditCommand toCommand(Long replyId, Long accountId) {
            return ReplyEditCommand.builder()
                    .accountId(accountId)
                    .replyId(replyId)
                    .contents(contents)
                    .isOnlyReadByAuthor(isReadOnlyAuthor)
                    .build();
        }
    }
}
