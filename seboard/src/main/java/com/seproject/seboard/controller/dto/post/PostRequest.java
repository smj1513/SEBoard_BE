package com.seproject.seboard.controller.dto.post;

import com.seproject.seboard.application.dto.post.PostCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostWriteCommand;
import com.seproject.seboard.controller.dto.user.AnonymousRequest;
import com.seproject.seboard.domain.model.exposeOptions.ExposeState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.seproject.seboard.application.dto.post.PostCommand.*;

public class PostRequest {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrievePostListRequest {
        private Long categoryId;
        private Integer page;
        private Integer perPage;

    }

    @Data
    public static class CreatePostRequest {
        private String title;
        private String contents;
        private List<MultipartFile> attachment;
        private Long categoryId;
        private boolean pined;
        private ExposeOptionRequest exposeOption;
        private boolean isAnonymous;

        public PostWriteCommand toCommand(Long accountId) {
            return PostWriteCommand.builder()
                    .title(title)
                    .contents(getContents())
                    .categoryId(categoryId)
                    .pined(pined)
                    .accountId(accountId)
                    .exposeState(ExposeState.valueOf(exposeOption.getName()))
                    .privatePassword(exposeOption.getPassword())
                    .anonymous(isAnonymous)
                    .build();
        }
    }

    @Data
    public static class UpdateNamedPostRequest {
        private String title;
        private String contents;
        private List<MultipartFile> attachment;
        private Long categoryId;
        private boolean pined;
        private ExposeOptionRequest exposeOption;

        public PostEditCommand toCommand(Long postId, Long accountId) {
            return PostEditCommand.builder()
                    .postId(postId)
                    .title(title)
                    .contents(getContents())
                    .categoryId(categoryId)
                    .pined(pined)
                    .accountId(accountId)
                    .exposeState(ExposeState.valueOf(exposeOption.getName()))
                    .privatePassword(exposeOption.getPassword())
                    .build();
        }
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
