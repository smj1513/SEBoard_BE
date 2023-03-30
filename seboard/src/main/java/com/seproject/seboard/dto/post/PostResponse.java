package com.seproject.seboard.dto.post;

import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.dto.PaginationResponse;
import com.seproject.seboard.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

        public static RetrievePostListResponseElement toDTO(Post post,int commentSize) {
            CategoryResponse categoryResponse = CategoryResponse.toDTO(post.getCategory());
            UserResponse userResponse = UserResponse.toDTO(post.getAuthor());
            BaseTime baseTime = post.getBaseTime();
            boolean hasAttachment = post.getAttachments() != null && post.getAttachments().size() != 0;
            return builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .category(categoryResponse)
                    .author(userResponse)
                    .views(post.getViews())
                    .createdAt(baseTime.getCreatedAt())
                    .modifiedAt(baseTime.getModifiedAt())
                    .hasAttachment(hasAttachment)
                    .commentSize(commentSize)
                    .pined(post.isPined())
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrievePostListResponse {
        private PaginationResponse paginationInfo;
        private List<RetrievePostListResponseElement> posts;

        public static RetrievePostListResponse toDTO(List<RetrievePostListResponseElement> posts,PaginationResponse paginationInfo) {

            return builder()
                    .paginationInfo(paginationInfo)
                    .posts(posts)
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrievePostResponse {
        @Schema(description = "게시물 pk")
        private Long postId;
        private String title;
        private CategoryResponse category;
        private UserResponse author;
        private Integer views;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        @Schema(description = "첨부파일을 가진 게시물인지")
        private boolean hasAttachment;
        @Schema(description = "댓글 개수")
        private Integer commentSize;
        @Schema(description = "공지설정 여부")
        private boolean pined;


        public static RetrievePostResponse toDTO(Post post,int commentSize) {
            CategoryResponse categoryResponse = CategoryResponse.toDTO(post.getCategory());
            UserResponse user = UserResponse.toDTO(post.getAuthor());
            BaseTime baseTime = post.getBaseTime();
            return builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .category(categoryResponse)
                    .author(user)
                    .views(post.getViews())
                    .createdAt(baseTime.getCreatedAt())
                    .modifiedAt(baseTime.getModifiedAt())
                    .hasAttachment(post.getAttachments() != null)
                    .commentSize(commentSize)
                    .pined(post.isPined())
                    .build();
        }


    }
}
