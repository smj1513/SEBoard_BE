package com.seproject.board.post.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExposeOptionRequest {
    private String name;
    private String password = "";
}
