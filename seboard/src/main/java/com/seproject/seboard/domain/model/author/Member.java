package com.seproject.seboard.domain.model.author;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class Member extends Author{
    private String loginId;
}
