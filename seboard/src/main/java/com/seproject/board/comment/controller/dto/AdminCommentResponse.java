package com.seproject.board.comment.controller.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.seproject.member.controller.dto.UserResponse;
import com.seproject.board.menu.domain.Menu;
import com.seproject.board.comment.domain.model.Comment;
import com.seproject.board.comment.domain.model.Reply;
import com.seproject.board.common.Status;
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
