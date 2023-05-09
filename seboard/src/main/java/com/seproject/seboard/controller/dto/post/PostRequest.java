package com.seproject.seboard.controller.dto.post;

import com.seproject.seboard.application.dto.post.PostCommand.PostWriteCommand;
import com.seproject.seboard.domain.model.post.exposeOptions.ExposeState;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    @NoArgsConstructor
    public static class RetrievePrivacyPostRequest{
        private String password;

        public RetrievePrivacyPostRequest(String password) {
            this.password = password;
        }
    }

    @Data
    @AllArgsConstructor
    public static class CreatePostRequest {
        @NotNull
        private String title;
        @NotNull
        private String contents;
        private List<Long> attachmentIds = new ArrayList<>();
        @NotNull
        private Long categoryId;
        @NotNull
        private boolean pined;
        @NotNull
        private ExposeOptionRequest exposeOption;
        @NotNull
        private boolean isAnonymous;

        public PostWriteCommand toCommand(String loginId) {
            return PostWriteCommand.builder()
                    .title(title)
                    .contents(getContents())
                    .categoryId(categoryId)
                    .pined(pined)
                    .loginId(loginId)
                    .exposeState(ExposeState.valueOf(exposeOption.getName()))
                    .privatePassword(exposeOption.getPassword())
                    .anonymous(isAnonymous)
                    .attachmentIds(attachmentIds)
                    .build();
        }
    }

    @Data
    public static class UpdatePostRequest {
        @NotNull
        private String title;
        @NotNull
        private String contents;
        private List<Long> attachmentIds = new ArrayList<>();
        @NotNull
        private Long categoryId;
        @NotNull
        private boolean pined;
        @NotNull
        private ExposeOptionRequest exposeOption;

        public PostEditCommand toCommand(Long postId, String loginId) {
            return PostEditCommand.builder()
                    .postId(postId)
                    .title(title)
                    .contents(getContents())
                    .categoryId(categoryId)
                    .pined(pined)
                    .loginId(loginId)
                    .exposeState(ExposeState.valueOf(exposeOption.getName()))
                    .privatePassword(exposeOption.getPassword())
                    .attachmentIds(attachmentIds)
                    .build();
        }
    }

}
