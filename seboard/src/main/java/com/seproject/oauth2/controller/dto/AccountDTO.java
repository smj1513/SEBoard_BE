package com.seproject.oauth2.controller.dto;

import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.model.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {

    @Data
    public static class RetrieveAllAccountRequest {
        private int page;
        private int perPage;
    }


    @Builder
    @Data
    public static class RetrieveAllAccountResponse {
        private int total;
        private int nowPage;
        private int perPage;

        private List<RetrieveAccountResponse> accounts;

        public static RetrieveAllAccountResponse toDTO(int total, int nowPage,int perPage,List<Account> accounts){

            return builder()
                    .total(total)
                    .nowPage(nowPage)
                    .perPage(perPage)
                    .accounts(accounts.stream().map(RetrieveAccountResponse::toDTO).collect(Collectors.toList()))
                    .build();

        }

    }
    @Builder
    @Data
    public static class RetrieveAccountResponse {
        private Long accountId;
        private String id;
        private String name;
        private String nickname;
        private LocalDateTime registeredDate;
        private List<Role> authorities;


        public static RetrieveAccountResponse toDTO(Account account) {
            return builder()
                    .accountId(account.getAccountId())
                    .id(account.getLoginId())
                    .name(account.getUsername())
                    .nickname(account.getNickname())
                    .registeredDate(account.getCreatedAt())
                    .authorities(account.getAuthorities())
                    .build();
        }

    }
}
