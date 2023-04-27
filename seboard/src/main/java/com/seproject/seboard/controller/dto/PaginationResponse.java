package com.seproject.seboard.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationResponse {
    private long totalAllSize;
    private long totalCommentSize;
    private boolean last;
    private int pageNum;
}
