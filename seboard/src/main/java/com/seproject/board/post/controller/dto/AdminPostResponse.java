package com.seproject.board.post.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seproject.board.post.controller.dto.PostResponse;
import com.seproject.member.controller.dto.UserResponse;
import com.seproject.board.menu.domain.Menu;
import com.seproject.board.common.Status;
import com.seproject.board.post.domain.model.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminPostResponse {
    @Data
    public static class MenuResponse{
        private Long menuId;
        private String name;
        private String urlId;

        public MenuResponse(Menu menu){
            this.menuId = menu.getMenuId();
            this.name = menu.getName();
            this.urlId = menu.getUrlInfo();
        }
    }

    @Data
    public static class AdminPostRetrieveResponse{
        private Long postId;
        private String title;
        private PostResponse.PostDetailCategoryResponse category;
        private MenuResponse menu;
        private UserResponse author;
        private Integer views;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private boolean hasAttachment;
        @JsonProperty("isReported")
        private boolean isReported;
        private String exposeOption;
        public AdminPostRetrieveResponse(Post post){
            this.postId = post.getPostId();
            this.title = post.getTitle();
            this.category = new PostResponse.PostDetailCategoryResponse(post.getCategory());
            this.menu = new MenuResponse(post.getCategory().getSuperMenu());
            this.author = new UserResponse(post.getAuthor());
            this.views = post.getViews();
            this.createdAt = post.getBaseTime().getCreatedAt();
            this.modifiedAt = post.getBaseTime().getModifiedAt();
            this.hasAttachment = post.hasAttachments();
            this.isReported = (post.getStatus()== Status.REPORTED);
            this.exposeOption = post.getExposeOption().getExposeState().toString();
        }

    }

    @Data
    public static class AdminDeletedPostResponse{
        private Long postId;
        private String title;
        private PostResponse.PostDetailCategoryResponse category;
        private MenuResponse menu;
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
            this.menu = new MenuResponse(post.getCategory().getSuperMenu());
            this.author = new UserResponse(post.getAuthor());
            this.views = post.getViews();
            this.createdAt = post.getBaseTime().getCreatedAt();
            this.modifiedAt = post.getBaseTime().getModifiedAt();
            this.hasAttachment = post.hasAttachments();
            this.exposeOption = post.getExposeOption().getExposeState().toString();
        }
    }
}
