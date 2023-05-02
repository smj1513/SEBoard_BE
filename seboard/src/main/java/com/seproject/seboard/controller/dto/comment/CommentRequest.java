package com.seproject.seboard.controller.dto.comment;

import com.seproject.seboard.application.dto.comment.CommentCommand.CommentEditCommand;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentWriteCommand;
import lombok.Data;

public class CommentRequest {

    @Data
    public static class CreateCommentRequest {
        private Long postId;
        private String contents;
        private boolean isAnonymous;
        private boolean isReadOnlyAuthor;

        public CommentWriteCommand toCommand(Long accountId) {
            return CommentWriteCommand.builder()
                    .postId(postId)
                    .accountId(accountId)
                    .contents(contents)
                    .isAnonymous(isAnonymous)
                    .isOnlyReadByAuthor(isReadOnlyAuthor)
                    .build();
        }
    }

    @Data
    public static class UpdateCommentRequest {
        private String contents;
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
