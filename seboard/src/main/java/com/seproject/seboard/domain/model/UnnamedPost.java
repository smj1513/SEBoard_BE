package com.seproject.seboard.domain.model;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class UnnamedPost extends Post {
    private String password;

    @Override
    public boolean isNamedPost() {
        return false;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
