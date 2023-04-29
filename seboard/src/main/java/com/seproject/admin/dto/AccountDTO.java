package com.seproject.admin.dto;

import com.seproject.account.model.Account;
import com.seproject.account.model.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {

    @Data
    public static class RetrieveAllAccountRequest {
        private int page;
        private int perPage;
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
    @Builder(access = AccessLevel.PRIVATE)
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


    @Data
    public static class CreateAccountRequest{
        private String id;
        private String password;
        private String email;
        private String name;
        private String nickname;
        private String profile;
        private List<String> authorities;

    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class CreateAccountResponse{
        private String id;
        private String email;
        private String name;
        private String nickname;
        private String profile;
        private List<Role> authorities;

        public static CreateAccountResponse toDTO(Account account){
            return builder()
                    .id(account.getLoginId())
                    .email(account.getEmail())
                    .name(account.getUsername())
                    .nickname(account.getNickname())
                    .profile(account.getProfile())
                    .authorities(account.getAuthorities())
                    .build();
        }
    }

    @Data
    public static class UpdateAccountRequest{
        private Long accountId;
        private String id;
        private String password;
        private String email;
        private String profile;
        private String name;
        private String nickname;
        private List<String> authorities;

    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class UpdateAccountResponse{
        private String id;
        private String email;
        private String name;
        private String nickname;
        private String profile;
        private List<Role> authorities;

        public static UpdateAccountResponse toDTO(Account account){
            return builder()
                    .id(account.getLoginId())
                    .email(account.getEmail())
                    .name(account.getUsername())
                    .nickname(account.getNickname())
                    .authorities(account.getAuthorities())
                    .profile(account.getProfile())
                    .build();
        }
    }

    @Data
    public static class DeleteAccountRequest{
        private Long accountId;
    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class DeleteAccountResponse{
        private String id;
        private String email;
        private String name;
        private String nickname;
        private String profile;
        private List<Role> authorities;

        public static DeleteAccountResponse toDTO(Account account){
            return builder()
                    .id(account.getLoginId())
                    .email(account.getEmail())
                    .name(account.getUsername())
                    .nickname(account.getNickname())
                    .authorities(account.getAuthorities())
                    .profile(account.getProfile())
                    .build();
        }
    }


}
