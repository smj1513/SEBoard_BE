package com.seproject.seboard.controller.dto.comment;

import com.seproject.seboard.controller.dto.user.AnonymousRequest;
import lombok.Data;

public class CommentRequest {

    @Data
    public static class CreateNamedCommentRequest {
        private Long postId;
        private String contents;
    }


    @Data
    public static class CreateUnnamedCommentRequest {
        private Long postId;
        private String contents;
        private AnonymousRequest author;

    }

    @Data
    public static class UpdateUnnamedCommentRequest {
        private String contents;
        private AnonymousRequest author;

    }


}
