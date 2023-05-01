package com.seproject.account.model;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_KUMOH = "ROLE_KUMOH";
    @Id
    @GeneratedValue
    private Long roleId;

    @Column(nullable=false, unique=true)
    private String name;

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return roleId;
    }
    @Override
    public String getAuthority() {
        return name;
    }
}
