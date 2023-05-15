package com.seproject.account.model.role.auth;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
@Entity
@Table(name = "admin_authorizations")
public class AdminAuthorization extends Authorization {

    @Override
    public String getType() {
        return "ADMIN";
    }
}
