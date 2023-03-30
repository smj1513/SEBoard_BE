package com.seproject.seboard.dto.comment;

import com.seproject.seboard.dto.user.AnonymousRequest;
import com.seproject.seboard.dto.user.TagAuthorRequest;
import lombok.Data;

public class ReplyRequest {

    @Data
    public static class CreateNamedReplyRequest {
        private Long commentId;
        private Long tag;
        private TagAuthorRequest tagAuthor;
        private String contents;
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
    public static class UpdateUnnamedReplyRequest {
        private String contents;
        private String password;
    }
}
