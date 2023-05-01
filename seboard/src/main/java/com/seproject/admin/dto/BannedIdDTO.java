package com.seproject.admin.dto;

import com.seproject.admin.domain.BannedId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class BannedIdDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveBannedIdResponse {
        private Long id;
        private String bannedId;


        public static RetrieveBannedIdResponse toDTO(BannedId bannedId){
            return RetrieveBannedIdResponse.builder()
                    .id(bannedId.getId())
                    .bannedId(bannedId.getBannedId())
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveAllBannedIdResponse {
        private List<RetrieveBannedIdResponse> bannedIds;

        public static RetrieveAllBannedIdResponse toDTO(List<BannedId> bannedIds){
            return builder()
                    .bannedIds(bannedIds.stream()
                            .map(RetrieveBannedIdResponse::toDTO)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    public static class CreateBannedIdRequest {
        private String bannedId;

    }

    @Data
    public static class DeleteBannedIdRequest {
        private String bannedId;

    }
}
