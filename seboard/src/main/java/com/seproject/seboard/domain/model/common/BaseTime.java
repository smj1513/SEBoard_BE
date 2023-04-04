package com.seproject.seboard.domain.model.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;


@Embeddable
@Getter
@AllArgsConstructor
public class BaseTime {
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static BaseTime now(){
        return new BaseTime(LocalDateTime.now(), LocalDateTime.now());
    }
}
