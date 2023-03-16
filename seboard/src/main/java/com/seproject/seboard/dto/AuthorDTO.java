package com.seproject.seboard.dto;

import com.seproject.seboard.domain.model.Author;
import lombok.Builder;

public class AuthorDTO {

    @Builder
    public static class AuthorResponseDTO {
        private String name;
        private String loginId;

        public static AuthorResponseDTO toDTO(Author author) {
            return builder()
                    .loginId(author.getLoginId())
                    .name(author.getName())
                    .build();
        }
    }
}
