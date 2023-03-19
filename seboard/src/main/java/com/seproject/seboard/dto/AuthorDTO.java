package com.seproject.seboard.dto;

import com.seproject.seboard.domain.model.Author;
import lombok.Builder;
import lombok.Data;

public class AuthorDTO {
    @Builder
    @Data
    public static class AnonymousVerifyingDTO{
        private String password;
    }

    @Builder
    @Data
    public static class AnonymousCreationDTO{
        private String name;
        private String password;
    }

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
