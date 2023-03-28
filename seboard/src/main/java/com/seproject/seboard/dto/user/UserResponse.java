package com.seproject.seboard.dto.user;

import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.model.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class UserResponse {

    private String loginId;
    private String name;


    public static UserResponse toDTO(User user) {
        String loginId =
                user.isAnonymous() ? "Anonymous" : ((Member)user).getLoginId();
        return builder()
                .loginId(loginId)
                .name(user.getName())
                .build();
    }

 }
