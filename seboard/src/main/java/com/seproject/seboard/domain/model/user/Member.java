package com.seproject.seboard.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="members")
public class Member extends User{

    private String loginId;

    @Override
    public boolean isAnonymous() {
        return false;
    }
}
