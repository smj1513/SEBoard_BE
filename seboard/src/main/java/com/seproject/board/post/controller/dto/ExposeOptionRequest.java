package com.seproject.board.post.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExposeOptionRequest {
    private String name;
    private String password = "";

    public ExposeOptionRequest(String name) {
        this.name = name;
        this.password = "";
    }

    public ExposeOptionRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
