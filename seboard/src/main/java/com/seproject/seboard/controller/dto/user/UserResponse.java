package com.seproject.seboard.controller.dto.user;

import com.seproject.seboard.domain.model.user.BoardUser;
import com.seproject.seboard.domain.model.user.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class UserResponse {

    private String loginId;
    private String name;


    public static UserResponse toDTO(BoardUser user) {
        String loginId =
                user.isAnonymous() ? "Anonymous" : ((Member)user).getAccount().getLoginId();
        return builder()
                .loginId(loginId)
                .name(user.getName())
                .build();
    }

 }
