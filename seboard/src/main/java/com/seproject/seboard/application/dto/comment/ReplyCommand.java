package com.seproject.seboard.application.dto.comment;

import lombok.Builder;
import lombok.Getter;

public class ReplyCommand {
    @Getter
    @Builder
    public static class ReplyWriteCommand{
        private Long postId;
        private Long superCommentId;
        private Long tagCommentId;
        private Long accountId;
        private String contents;
        private boolean isAnonymous;
        private boolean isOnlyReadByAuthor;
    }

    @Getter
    @Builder
    public static class ReplyEditCommand{
        private Long replyId;
        private Long accountId;
        private String contents;
        private boolean isOnlyReadByAuthor;
    }
}
