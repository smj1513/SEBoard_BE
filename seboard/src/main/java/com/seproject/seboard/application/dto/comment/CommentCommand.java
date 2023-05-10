package com.seproject.seboard.application.dto.comment;

import lombok.Builder;
import lombok.Data;

public class CommentCommand {
    @Data
    @Builder
    public static class CommentWriteCommand{
        private Long postId;
        private String loginId;
        private String contents;
        private boolean isAnonymous;
        private boolean isOnlyReadByAuthor;
    }

    @Data
    @Builder
    public static class CommentEditCommand{
        private Long commentId;
        private String loginId;
        private String contents;
        private boolean isOnlyReadByAuthor;
    }

    @Data
    @Builder
    public static class CommentListFindCommand {
        private Long postId;
        private String loginId;
        private Integer page;
        private Integer perPage;
    }
}
