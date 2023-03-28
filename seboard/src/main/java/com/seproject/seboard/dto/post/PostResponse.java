package com.seproject.seboard.dto.post;

import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.dto.user.UserResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PostResponse {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrievePostListResponse {
        private Map<String,Object> paginationInfo;
        private List<RetrievePostResponse> posts;

        public static RetrievePostListResponse toDTO(List<RetrievePostResponse> posts,Map<String,Object> paginationInfo) {

            return builder()
                    .paginationInfo(paginationInfo)
                    .posts(posts)
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrievePostResponse {
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
