package com.seproject.account.account.controller.dto;

import com.seproject.account.account.domain.Account;
import com.seproject.account.role.domain.Role;
import com.seproject.account.role.domain.RoleAccount;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class WithDrawDTO {

    @Data
    public static class WithDrawRequest {
        private String refreshToken;
    }


    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class WithDrawResponse {
        private boolean requiredRedirect;
        private String url;

        private Long accountId;
        private String loginId;
        private List<String> roles;
        private String nickname;
        private String name;
        public static WithDrawResponse toDTO(Account account, boolean requiredRedirect, String url) {
            return builder()
                    .requiredRedirect(requiredRedirect)
                    .url(url)
                    .accountId(account.getAccountId())
                    .loginId(account.getLoginId())
                    .roles(account.getRoleAccounts().stream()
                            .map(RoleAccount::getRole)
                            .map(Role::toString)
                            .collect(Collectors.toList()))
                    .nickname(account.getNickname())
                    .name(account.getName())
                    .build();
        }

    }
}
