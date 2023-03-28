package com.seproject.seboard.dto.post;

import com.seproject.seboard.dto.user.AnonymousRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostRequest {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrievePostListRequest {
        private Long categoryId;
        private Integer page;
        private Integer perPage;

    }

    @Data
    public static class CreateNamedPostRequest {
        private String title;
        private String contents;
        private List<MultipartFile> attachment;
        private Long categoryId;
        private boolean pined;

    }

    @Data
    public static class UpdateNamedPostRequest {
        private String title;
        private String contents;
        private List<MultipartFile> attachment;
        private Long categoryId;
        private boolean pined;

    }

    @Data
    public static class CreateUnnamedPostRequest {
        private String title;
        private String contents;
        private List<MultipartFile> attachment;
        private Long categoryId;
        private AnonymousRequest author;

    }

    @Data
    public static class UpdateUnnamedPostRequest {
        private String title;
        private String contents;
        private List<MultipartFile> attachment;
        private Long categoryId;
        private AnonymousRequest author;

    }


}
