package com.seproject.seboard.controller.dto.comment;

import com.seproject.seboard.controller.dto.post.ExposeOptionRequest;
import com.seproject.seboard.controller.dto.user.AnonymousRequest;
import com.seproject.seboard.controller.dto.user.TagAuthorRequest;
import com.seproject.seboard.domain.model.exposeOptions.ExposeState;
import lombok.Builder;
import lombok.Data;

import static com.seproject.seboard.application.dto.comment.ReplyCommand.*;

public class ReplyRequest {

    @Data
    public static class CreateReplyRequest {
        private Long postId;
        private Long superCommentId;
        private Long tagCommentId;
        private boolean isAnonymous;
        private String contents;
        private ExposeOptionRequest exposeOption;

        public ReplyWriteCommand toCommand(Long accountId) {
            return ReplyWriteCommand.builder()
                    .postId(postId)
                    .accountId(accountId)
                    .superCommentId(superCommentId)
                    .tagCommentId(tagCommentId)
                    .contents(contents)
                    .isAnonymous(isAnonymous)
                    .exposeState(ExposeState.valueOf(exposeOption.getName()))
                    .exposePassword(exposeOption.getPassword())
                    .build();
        }
    }


    @Data
    public static class CreateUnnamedReplyRequest {
        private Long commentId;
        private Long tag;
        private TagAuthorRequest tagAuthor;
        private String contents;
        private AnonymousRequest author;
    }

    @Data
    public static class UpdateReplyRequest {
        private String contents;
        private ExposeOptionRequest exposeOption;

        public ReplyEditCommand toCommand(Long replyId, Long accountId) {
            return ReplyEditCommand.builder()
                    .accountId(accountId)
                    .replyId(replyId)
                    .contents(contents)
                    .exposeState(ExposeState.valueOf(exposeOption.getName()))
                    .exposePassword(exposeOption.getPassword())
                    .build();
        }
    }
}
