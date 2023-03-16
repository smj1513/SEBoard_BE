package com.seproject.seboard.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class BaseTime {
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}
