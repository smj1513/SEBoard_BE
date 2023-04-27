package com.seproject.seboard.domain.model.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;


@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class BaseTime {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static BaseTime now(){
        return new BaseTime(LocalDateTime.now(), LocalDateTime.now());
    }
}
