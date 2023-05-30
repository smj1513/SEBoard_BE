package com.seproject.admin.controller.dto.comment;


import com.seproject.seboard.controller.dto.user.UserResponse;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import lombok.Data;

import java.time.LocalDateTime;

public class AdminCommentResponse {
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
        private boolean isReported;

        public AdminCommentListResponse(Comment comment){
            this.commentId = comment.getCommentId();
            this.postId = comment.getPost().getPostId();
            this.author = new UserResponse(comment.getAuthor());
            this.contents = comment.getContents();
            this.createdAt = comment.getBaseTime().getCreatedAt();
            this.modifiedAt = comment.getBaseTime().getModifiedAt();

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
}
