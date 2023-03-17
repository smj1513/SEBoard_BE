package com.seproject.seboard.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Bookmark {
    private Long bookmarkId;
    private Long postId;
    private Long userId;
}