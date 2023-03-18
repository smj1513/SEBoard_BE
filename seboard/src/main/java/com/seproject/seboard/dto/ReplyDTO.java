package com.seproject.seboard.dto;

import com.seproject.seboard.domain.model.Author;
import com.seproject.seboard.domain.model.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class ReplyDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class ReplyResponseDTO {
        private Long commentId;
        private AuthorDTO.AuthorResponseDTO author;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String contents;
        private boolean isEditable;

        public static ReplyResponseDTO toDTO(Comment comment, boolean isEditable) {
            Author author = comment.getAuthor();

            return builder().commentId(comment.getCommentId())
                    .author(AuthorDTO.AuthorResponseDTO.toDTO(author))
                    .createdAt(comment.getBaseTime().getCreatedAt())
                    .modifiedAt(comment.getBaseTime().getModifiedAt())
                    .contents(comment.getContents())
                    .isEditable(isEditable)
                    .build();
        }

    }
}
