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
        this.password = password;
    }
}
