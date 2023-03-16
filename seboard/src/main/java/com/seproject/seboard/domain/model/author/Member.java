package com.seproject.seboard.domain.model.author;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Member extends Author{
    private String loginId;

    @Override
    public boolean isAnonymous() {
        return false;
    }
}
