package com.seproject.seboard.application.dto.comment;

import lombok.Builder;
import lombok.Data;

public class CommentCommand {
    @Data
    @Builder
    public static class CommentListFindCommand {
        private Long postId;
        private Long accountId;
        private Integer page;
        private Integer perPage;
    }
}
