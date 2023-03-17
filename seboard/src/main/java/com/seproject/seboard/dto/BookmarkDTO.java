package com.seproject.seboard.dto;

import com.seproject.seboard.domain.model.Author;
import com.seproject.seboard.domain.model.Category;
import com.seproject.seboard.domain.model.Post;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

public class BookmarkDTO {

    @Builder(access = AccessLevel.PRIVATE)
    public static class BookmarkListResponseDTO {
        private Long bookmarkId; // bookmark id도 굳이 보내줄 필요는 없을듯
        private Long postId;
        private String title;
        private CategoryDTO.CategoryResponseDTO category;
        private AuthorDTO.AuthorResponseDTO author;
        private int views;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private boolean hasAttachment;
        private int commentSize;
        private boolean pined; // 북마크 목록 조회를 하게 되면 pined는 의미가 없는듯

        public static BookmarkDTO.BookmarkListResponseDTO toDTO(Post post, int commentSize){
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
                    .build();
        }
    }
}
