package com.seproject.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogoutDTO {

    private boolean requiredRedirect;
    private String url;

}
