package com.seproject.seboard.controller.dto.comment;

import com.seproject.seboard.application.dto.comment.CommentCommand;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentEditCommand;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentWriteCommand;
import com.seproject.seboard.controller.dto.post.ExposeOptionRequest;
import com.seproject.seboard.controller.dto.user.AnonymousRequest;
import com.seproject.seboard.domain.model.exposeOptions.ExposeOption;
import com.seproject.seboard.domain.model.exposeOptions.ExposeState;
import lombok.Data;

public class CommentRequest {

    @Data
    public static class CreateCommentRequest {
        private Long postId;
        private String contents;
        private boolean isAnonymous;
        private ExposeOptionRequest exposeOption;

        public CommentWriteCommand toCommand(Long accountId) {
            return CommentWriteCommand.builder()
                    .postId(postId)
                    .accountId(accountId)
                    .contents(contents)
                    .isAnonymous(isAnonymous)
                    .exposeState(ExposeState.valueOf(exposeOption.getName()))
                    .exposePassword(exposeOption.getPassword())
                    .build();
        }
    }

    @Data
    public static class UpdateCommentRequest {
        private String contents;
        private ExposeOptionRequest exposeOption;
        public CommentEditCommand toCommand(Long commentId, Long accountId) {
            return CommentEditCommand.builder()
                    .accountId(accountId)
                    .commentId(commentId)
                    .contents(contents)
                    .exposeState(ExposeState.valueOf(exposeOption.getName()))
                    .exposePassword(exposeOption.getPassword())
                    .build();
        }
    }


}
