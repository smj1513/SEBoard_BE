package com.seproject.member.controller.dto;

import com.seproject.member.domain.BoardUser;
import lombok.Data;

@Data
public class UserResponse {

    private String loginId;
    private String name;

    public UserResponse(String loginId, String name) {
        this.loginId = loginId;
        this.name = name;
    }

    public UserResponse(BoardUser boardUser) {
        this.loginId = boardUser.getLoginId();
        this.name = boardUser.getName();
    }
}
