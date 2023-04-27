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
        private boolean isReadOnlyAuthor;

        public ReplyWriteCommand toCommand(Long accountId) {
            return ReplyWriteCommand.builder()
                    .postId(postId)
                    .accountId(accountId)
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
