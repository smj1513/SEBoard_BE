package com.seproject.seboard.application.dto.post;

import com.seproject.seboard.domain.model.post.exposeOptions.ExposeState;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class PostCommand {
    @Data
    @Builder
    public static class PostWriteCommand {
        private String title;
        private String contents;
        private Long categoryId;
        private Long accountId;
        private boolean pined;
        private ExposeState exposeState;
        private String privatePassword;
        private boolean anonymous;
        private List<Long> attachmentIds;
    }

    @Data
    @Builder
    public static class PostEditCommand{
        private Long postId;
        private String title;
        private String contents;
        private Long categoryId;
        private Long accountId;
        private boolean pined;
        private ExposeState exposeState;
        private String privatePassword;
        private List<Long> attachmentIds;
    }

    @Data
    @Builder
    public static class PostListFindCommand {
        private Long categoryId;
        private int page;
        private int size;
    }
}
