package com.seproject.seboard.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seproject.seboard.controller.dto.FileMetaDataResponse.FileMetaDataListResponse;
import com.seproject.seboard.controller.dto.PaginationResponse;
import com.seproject.seboard.controller.dto.user.UserResponse;
import com.seproject.seboard.domain.model.post.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostResponse {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrievePostListResponseElement {
        private Long postId;
        private String title;
        private CategoryResponse category;
        private UserResponse author;
        private Integer views;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private boolean hasAttachment;
        private Integer commentSize;
        private boolean pined;


//        public static RetrievePostListResponseElement toDTO(Post post, int commentSize) {
//            CategoryResponse categoryResponse = CategoryResponse.toDTO(post.getCategory());
//            UserResponse userResponse = UserResponse.toDTO(post.getAuthor());
//            BaseTime baseTime = post.getBaseTime();
//            boolean hasAttachment = post.getAttachments() != null && post.getAttachments().size() != 0;
//            return builder()
//                    .postId(post.getPostId())
//                    .title(post.getTitle())
//                    .category(categoryResponse)
//                    .author(userResponse)
//                    .views(post.getViews())
//                    .createdAt(baseTime.getCreatedAt())
//                    .modifiedAt(baseTime.getModifiedAt())
//                    .hasAttachment(hasAttachment)
//                    .commentSize(commentSize)
//                    .pined(post.isPined())
//                    .build();
//        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrievePinedPostListResponse {
        private RetrievePostListResponse pined;
        private RetrievePostListResponse normal;

        public static RetrievePinedPostListResponse toDTO(RetrievePostListResponse pined, RetrievePostListResponse normal) {
            return builder()
                    .pined(pined)
                    .normal(normal)
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrievePostListResponse {
        private PaginationResponse paginationInfo;
        private List<RetrievePostListResponseElement> posts;

        public static RetrievePostListResponse toDTO(List<RetrievePostListResponseElement> posts, PaginationResponse paginationInfo) {

            return builder()
                    .paginationInfo(paginationInfo)
                    .posts(posts)
                    .build();
        }
    }

    @Data
    public static class RetrievePostDetailResponse {
        @Schema(description = "게시물 pk")
        private Long postId;
        private String title;
        private String contents;
        private CategoryResponse category;
        private UserResponse author;
        private Integer views;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        @JsonProperty("isEditable")
        private boolean isEditable;
        @JsonProperty("isBookmarked")
        private boolean isBookmarked;
        @JsonProperty("isPined")
        private boolean isPined;
        private String exposeType;
        private FileMetaDataListResponse attachments;


        public RetrievePostDetailResponse(Post post) {
            this.postId = post.getPostId();
            this.title = post.getTitle();
            this.contents = post.getContents();
            this.category = new CategoryResponse(post.getMenu().getMenuId(), post.getMenu().getName());
            this.author = new UserResponse(post.getAuthor());
            this.views = post.getViews();
            this.createdAt = post.getBaseTime().getCreatedAt();
            this.modifiedAt = post.getBaseTime().getModifiedAt();
            this.exposeType = post.getExposeOption().getExposeState().toString();
            this.attachments = new FileMetaDataListResponse(new ArrayList<>(post.getAttachments()));
            this.isPined = post.isPined();
        }
    }
}
