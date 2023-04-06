package com.seproject.seboard.application.dto.post;

import com.seproject.seboard.domain.model.exposeOptions.ExposeState;
import lombok.Builder;
import lombok.Data;

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
    }

    @Data
    @Builder
    public static class PostRemoveCommand{
        private Long postId;
        private Long accountId;
    }
}
