package com.seproject.seboard.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="anonymous")
public class Anonymous extends User {
    private String password;

    @Override
    public boolean isAnonymous() {
        return true;
    }
}
