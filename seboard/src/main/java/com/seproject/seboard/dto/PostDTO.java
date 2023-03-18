package com.seproject.seboard.dto;

import com.seproject.seboard.domain.model.Author;
import com.seproject.seboard.domain.model.BaseTime;
import com.seproject.seboard.domain.model.Category;
import com.seproject.seboard.domain.model.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


public class PostDTO {
    @Builder
    @Data
    public static class UnnamedPostUpdatingRequestDTO {
        private String title;
        private String contents;
        private Long categoryId;
        private AuthorDTO.AnonymousVerifyingDTO author;
    }

    @Builder
    @Data
    public static class UnnamedPostCreationRequestDTO {
        private String title;
        private String contents;
        private Long categoryId;
        private AuthorDTO.AnonymousCreationDTO author;
    }


    @Builder
    @Data
    public static class NamedPostRequestDTO {
        private String title;
        private String contents;
        private Long categoryId;
        private boolean pined;
    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class PostListResponseDTO {
        private Long postId;
        private String title;

        private CategoryDTO.CategoryResponseDTO category;
        private AuthorDTO.AuthorResponseDTO author;

        private int views;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private boolean hasAttachment;
        private int commentSize;
        private boolean pined;

        public static PostListResponseDTO toDTO(Post post, int commentSize){
            Author author = post.getAuthor();
            Category category = post.getCategory();

            CategoryDTO.CategoryResponseDTO categoryResponseDTO = CategoryDTO.CategoryResponseDTO.toDTO(category);
            AuthorDTO.AuthorResponseDTO authorResponseDTO = AuthorDTO.AuthorResponseDTO.toDTO(author);

            return builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .views(post.getViews())
                    .author(authorResponseDTO)
                    .category(categoryResponseDTO)
                    .createdAt(post.getBaseTime().getCreatedAt())
                    .modifiedAt(post.getBaseTime().getModifiedAt())
                    .commentSize(commentSize)
                    .pined(post.isPined())
                    .build();
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class PostResponseDTO {
        private Long postId;
        private String title;
        private String contents;
        private int views;

        private AuthorDTO.AuthorResponseDTO author;
        private CategoryDTO.CategoryResponseDTO category;

        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private boolean isEditable;
        private boolean bookmarked;
//    private Object attachment;


        public static PostResponseDTO toDTO(Post post,boolean isEditable,boolean bookmarked){
            Author author = post.getAuthor();
            Category category = post.getCategory();

            CategoryDTO.CategoryResponseDTO categoryResponseDTO = CategoryDTO.CategoryResponseDTO.toDTO(category);
            AuthorDTO.AuthorResponseDTO authorResponseDTO = AuthorDTO.AuthorResponseDTO.toDTO(author);

            return builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .contents(post.getContents())
                    .views(post.getViews())
                    .author(authorResponseDTO)
                    .category(categoryResponseDTO)
                    .createdAt(post.getBaseTime().getCreatedAt())
                    .modifiedAt(post.getBaseTime().getModifiedAt())
                    .isEditable(isEditable)
                    .bookmarked(bookmarked)
                    .build();
        }
    }

}
