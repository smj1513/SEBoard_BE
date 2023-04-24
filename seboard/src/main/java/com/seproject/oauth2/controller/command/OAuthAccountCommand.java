package com.seproject.oauth2.controller.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OAuthAccountCommand {
    private String id;
    private String provider;
    private String email;
    private String nickname;
    private String name;
    private String profile;
}
