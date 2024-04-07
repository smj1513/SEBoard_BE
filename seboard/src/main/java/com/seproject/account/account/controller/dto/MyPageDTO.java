package com.seproject.account.account.controller.dto;

import com.seproject.account.account.domain.Account;
import com.seproject.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class MyPageDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class MyInfoResponse {
        private String nickname;

        // TODO : 필드 변경
        private Long boardUserId;
        private List<String> roles;

        public static MyInfoResponse toDTO(Member findMember,List<String> roles) {
            return builder()
                    .nickname(findMember.getName())
                    .roles(roles)
                    .boardUserId(findMember.getBoardUserId())
                    .build();
        }
    }

    @Data
    public static class MyInfoChangeRequest {
        private String nickname;

    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class MyInfoChangeResponse {
        private String nickname;

        public static MyInfoChangeResponse toDTO(Member member) {
            return builder()
                    .nickname(member.getName())
                    .build();
        }
    }
}
