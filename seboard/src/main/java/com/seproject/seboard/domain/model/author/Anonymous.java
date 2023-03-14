package com.seproject.seboard.domain.model.author;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class Anonymous extends Author{
    private String password;


    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }
}
