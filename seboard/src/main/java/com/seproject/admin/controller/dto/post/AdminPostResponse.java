package com.seproject.admin.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seproject.seboard.controller.dto.post.PostResponse;
import com.seproject.seboard.controller.dto.user.UserResponse;
import com.seproject.seboard.domain.model.common.Status;
import com.seproject.seboard.domain.model.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminPostResponse {
    private Long postId;
    private String title;
    private PostResponse.PostDetailCategoryResponse category;
    private UserResponse author;
    private Integer views;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean hasAttachment;
    @JsonProperty("isReported")
    private boolean isReported;
    private String exposeOption;
    public AdminPostResponse(Post post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.category = new PostResponse.PostDetailCategoryResponse(post.getCategory());
        this.author = new UserResponse(post.getAuthor());
        this.views = post.getViews();
        this.createdAt = post.getBaseTime().getCreatedAt();
        this.modifiedAt = post.getBaseTime().getModifiedAt();
        this.hasAttachment = post.hasAttachments();
        this.isReported = (post.getStatus()== Status.REPORTED);
        this.exposeOption = post.getExposeOption().getExposeState().toString();
    }

    @Data
    public static class AdminDeletedPostResponse{
        private Long postId;
        private String title;
        private PostResponse.PostDetailCategoryResponse category;
        private UserResponse author;
        private Integer views;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private boolean hasAttachment;
        private String exposeOption;
        public AdminDeletedPostResponse(Post post){
            this.postId = post.getPostId();
            this.title = post.getTitle();
            this.category = new PostResponse.PostDetailCategoryResponse(post.getCategory());
            this.author = new UserResponse(post.getAuthor());
            this.views = post.getViews();
            this.createdAt = post.getBaseTime().getCreatedAt();
            this.modifiedAt = post.getBaseTime().getModifiedAt();
            this.hasAttachment = post.hasAttachments();
            this.exposeOption = post.getExposeOption().getExposeState().toString();
        }
    }
}
