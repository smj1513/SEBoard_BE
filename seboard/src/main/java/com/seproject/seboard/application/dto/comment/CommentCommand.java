package com.seproject.seboard.application.dto.comment;

import com.seproject.seboard.domain.model.exposeOptions.ExposeState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class CommentCommand {
    @Data
    @Builder
    public static class CommentWriteCommand{
        private Long postId;
        private Long accountId;
        private String contents;
        private boolean isAnonymous;
        private ExposeState exposeState;
        private String exposePassword;
    }

    @Data
    @Builder
    public static class CommentEditCommand{
        private Long commentId;
        private Long accountId;
        private String contents;
        private ExposeState exposeState;
        private String exposePassword;
    }

    @Data
    @Builder
    public static class CommentListFindCommand {
        private Long postId;
        private Long accountId;
        private Integer page;
        private Integer perPage;
    }
}
