package com.seproject.seboard.dto;

import com.seproject.seboard.domain.model.Author;
import com.seproject.seboard.domain.model.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CommentDTO {

    @Data
    @Builder
    public static class CommentListResponseDTO {

        private Long commentId;
        private AuthorDTO.AuthorResponseDTO author;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String contents;
        private boolean isEditable;
        private List<ReplyDTO.ReplyResponseDTO> replies;

        public static CommentListResponseDTO toDTO(Comment comment, boolean isEditable, List<ReplyDTO.ReplyResponseDTO> repliesDTO) {
            Author author = comment.getAuthor();
            return builder()
                    .commentId(comment.getCommentId())
                    .author(AuthorDTO.AuthorResponseDTO.toDTO(author))
                    .createdAt(comment.getBaseTime().getCreatedAt())
                    .modifiedAt(comment.getBaseTime().getModifiedAt())
                    .contents(comment.getContents())
                    .isEditable(isEditable)
                    .replies(repliesDTO)
                    .build();
        }
    }


}
