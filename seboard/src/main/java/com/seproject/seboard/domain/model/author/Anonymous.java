package com.seproject.seboard.domain.model.author;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Anonymous extends Author{

    private String password;

    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }


    @Override
    public boolean isAnonymous() {
        return true;
    }
}
