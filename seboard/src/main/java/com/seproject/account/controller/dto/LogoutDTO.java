package com.seproject.account.controller.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LogoutDTO {

    private boolean requiredRedirect;
    private String url;

}
