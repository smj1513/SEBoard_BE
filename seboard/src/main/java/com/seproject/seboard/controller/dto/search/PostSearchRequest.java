package com.seproject.seboard.controller.dto.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostSearchRequest {
    private Long categoryId;
    private String searchOption;
    private String query;
}
