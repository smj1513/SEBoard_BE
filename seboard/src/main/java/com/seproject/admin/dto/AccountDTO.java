package com.seproject.admin.dto;

import com.seproject.account.model.account.Account;
import com.seproject.account.model.role.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {

    @Data
    public static class AdminBulkAccountRequest{
        private List<Long> accountIds;
    }

    @Data
    public static class AdminRetrieveAccountCondition{
    }

    @Builder(access = AccessLevel.PRIVATE)
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
    @AllArgsConstructor
    @Data
    public static class RetrieveAccountResponse {
        private Long accountId;
        private String loginId;
        private String name;
        private String nickname;
        private LocalDateTime registeredDate;
        private List<Role> authorities;

        public RetrieveAccountResponse(Account account){
            this.accountId = account.getAccountId();
            this.loginId = account.getLoginId();
            this.name = account.getName();
            this.nickname = account.getNickname();
            this.registeredDate = account.getCreatedAt();
            this.authorities = account.getAuthorities();
        }


        public static RetrieveAccountResponse toDTO(Account account) {
            return builder()
                    .accountId(account.getAccountId())
                    .loginId(account.getLoginId())
                    .name(account.getName())
                    .nickname(account.getNickname())
                    .registeredDate(account.getCreatedAt())
                    .authorities(account.getAuthorities())
                    .build();
        }

    }


    @Data
    public static class CreateAccountRequest{
        private String id;
        private String password;
        private String name;
        private String nickname;
        private List<String> authorities;

    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class CreateAccountResponse{
        private String id;
        private String name;
        private String nickname;
        private List<Role> authorities;

        public static CreateAccountResponse toDTO(Account account){
            return builder()
                    .id(account.getLoginId())
                    .name(account.getName())
                    .nickname(account.getNickname())
                    .authorities(account.getAuthorities())
                    .build();
        }
    }

    @Data
    public static class UpdateAccountRequest{
        private String id;
        private String password;
        private String name;
        private String nickname;
        private List<String> authorities;

    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class UpdateAccountResponse{
        private String id;
        private String name;
        private String nickname;
        private List<Role> authorities;

        public static UpdateAccountResponse toDTO(Account account){
            return builder()
                    .id(account.getLoginId())
                    .name(account.getName())
                    .nickname(account.getNickname())
                    .authorities(account.getAuthorities())
                    .build();
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class DeleteAccountResponse{
        private String id;
        private String name;
        private String nickname;
        private List<Role> authorities;

        public static DeleteAccountResponse toDTO(Account account){
            return builder()
                    .id(account.getLoginId())
                    .name(account.getName())
                    .nickname(account.getNickname())
                    .authorities(account.getAuthorities())
                    .build();
        }
    }


}
