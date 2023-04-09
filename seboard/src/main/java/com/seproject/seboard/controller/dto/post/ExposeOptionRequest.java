package com.seproject.seboard.controller.dto.post;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExposeOptionRequest {
    private String name;
    private String password = "";
}
