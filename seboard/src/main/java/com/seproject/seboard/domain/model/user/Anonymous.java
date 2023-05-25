package com.seproject.seboard.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@SuperBuilder
@AllArgsConstructor
@Entity
@Table(name="anonymous")
@PrimaryKeyJoinColumn(name="anonymous_id")
public class Anonymous extends BoardUser {

    @Override
    public boolean isAnonymous() {
        return true;
    }
}
