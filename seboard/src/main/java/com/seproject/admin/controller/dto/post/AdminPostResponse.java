package com.seproject.admin.controller.dto.post;

import com.seproject.seboard.controller.dto.post.PostResponse;
import com.seproject.seboard.controller.dto.user.UserResponse;
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
    public AdminPostResponse(Post post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.category = new PostResponse.PostDetailCategoryResponse(post.getCategory());
        this.author = new UserResponse(post.getAuthor());
        this.views = post.getViews();
        this.createdAt = post.getBaseTime().getCreatedAt();
        this.modifiedAt = post.getBaseTime().getModifiedAt();
        this.hasAttachment = post.hasAttachments();
    }
}
