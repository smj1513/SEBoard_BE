package com.seproject.seboard.controller.dto.profile;

import lombok.Builder;
import lombok.Data;

public class ProfileResponse {
    @Data
    @Builder
    public static class ProfileInfoResponse{
        private String nickname;
        private Integer postCount;
        private Integer commentCount;
        private Integer bookmarkCount;
    }
}
