package com.seproject.seboard.controller.dto.search;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
public class PostSearchRequest {
    private Long categoryId;
    private String searchOption;
    private String query;
}
