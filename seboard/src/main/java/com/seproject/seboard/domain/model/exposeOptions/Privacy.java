package com.seproject.seboard.domain.model.exposeOptions;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Entity
@Table(name="privacies")
public class Privacy extends ExposeOption{
    private String password;

    public Privacy(String password) {
        super(ExposeState.PRIVACY);
        this.password = password;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
