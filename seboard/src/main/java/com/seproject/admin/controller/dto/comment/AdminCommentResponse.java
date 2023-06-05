package com.seproject.admin.controller.dto.comment;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.seproject.seboard.controller.dto.user.UserResponse;
import com.seproject.seboard.domain.model.category.Menu;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import com.seproject.seboard.domain.model.common.Status;
import lombok.Data;

import java.time.LocalDateTime;

public class AdminCommentResponse {
    @Data
    public static class AdminDeletedCommentResponse{
        private Long commentId;
        private Long superCommentId;
        private Long tagCommentId;
        private Long postId;
        private UserResponse author;
        private String contents;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        @JsonProperty("isReadOnlyAuthor")
        private boolean isReadOnlyAuthor;
        private MenuResponse menu;

        public AdminDeletedCommentResponse(Comment comment){
            this.commentId = comment.getCommentId();
            this.postId = comment.getPost().getPostId();
            this.author = new UserResponse(comment.getAuthor());
            this.contents = comment.getContents();
            this.createdAt = comment.getBaseTime().getCreatedAt();
            this.modifiedAt = comment.getBaseTime().getModifiedAt();
            this.isReadOnlyAuthor = comment.isOnlyReadByAuthor();
            this.menu = new MenuResponse(comment.getPost().getCategory().getSuperMenu());

            if(comment instanceof Reply){
                Reply reply = (Reply) comment;
                this.superCommentId = reply.getSuperComment().getCommentId();
                this.tagCommentId = reply.getTag().getCommentId();
            }else{
                this.superCommentId = null;
                this.tagCommentId = null;
            }
        }
    }

    @Data
    public static class AdminCommentListResponse{
        private Long commentId;
        private Long superCommentId;
        private Long tagCommentId;
        private Long postId;
        private UserResponse author;
        private String contents;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        @JsonProperty("isReported")
        private boolean isReported;
        @JsonProperty("isReadOnlyAuthor")
        private boolean isReadOnlyAuthor;
        private MenuResponse menu;

        public AdminCommentListResponse(Comment comment){
            this.commentId = comment.getCommentId();
            this.postId = comment.getPost().getPostId();
            this.author = new UserResponse(comment.getAuthor());
            this.contents = comment.getContents();
            this.createdAt = comment.getBaseTime().getCreatedAt();
            this.modifiedAt = comment.getBaseTime().getModifiedAt();
            this.isReported = (comment.getStatus()== Status.REPORTED);
            this.isReadOnlyAuthor = comment.isOnlyReadByAuthor();
            this.menu = new MenuResponse(comment.getPost().getCategory().getSuperMenu());

            if(comment instanceof Reply){
                Reply reply = (Reply) comment;
                this.superCommentId = reply.getSuperComment().getCommentId();
                this.tagCommentId = reply.getTag().getCommentId();
            }else{
                this.superCommentId = null;
                this.tagCommentId = null;
            }
        }
    }

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
}
