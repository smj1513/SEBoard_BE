package com.seproject.seboard.controller.dto.user;

import com.seproject.seboard.domain.model.user.BoardUser;
import com.seproject.seboard.domain.model.user.Member;
import lombok.AccessLevel;
import lombok.Builder;
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
