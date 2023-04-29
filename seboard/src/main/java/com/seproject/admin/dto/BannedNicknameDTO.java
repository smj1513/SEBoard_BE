package com.seproject.admin.dto;

import com.seproject.admin.domain.BannedNickname;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class BannedNicknameDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveBannedNicknameResponse {
        private Long id;
        private String bannedNickname;

        public static RetrieveBannedNicknameResponse toDTO(BannedNickname bannedNickname) {
            return builder()
                    .bannedNickname(bannedNickname.getBannedNickname())
                    .id(bannedNickname.getId())
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveAllBannedNicknameResponse {
        private List<RetrieveBannedNicknameResponse> nicknames;

        public static RetrieveAllBannedNicknameResponse toDTO(List<BannedNickname> bannedNicknames) {
            return builder()
                    .nicknames(bannedNicknames.stream()
                            .map(RetrieveBannedNicknameResponse::toDTO)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    public static class CreateBannedNicknameRequest {
        private String nickname;
    }

    @Data
    public static class DeleteBannedNicknameRequest {
        private String nickname;
    }

}
