package com.seproject.oauth2.controller.command;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AccountRegisterCommand {

    private String id;
    private String password;
    private String nickname;
    private String name;


}
