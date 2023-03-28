package com.seproject.seboard.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
public class UserRequest {

    private String name;
    private String password;

}
