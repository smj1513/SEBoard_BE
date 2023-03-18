package com.seproject.seboard.domain.model;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class UnnamedComment extends Comment {
    private String password;

    @Override
    public boolean isNamed() {
        return false;
    }
}
