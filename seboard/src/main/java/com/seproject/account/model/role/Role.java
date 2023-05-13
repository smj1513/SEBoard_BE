package com.seproject.account.model.role;

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

    @Override
    public String toString() {
        switch (name) {
            case ROLE_ADMIN: return "관리자";
            case ROLE_KUMOH: return "정회원";
            case ROLE_USER: return "준회원";
        }

        return "무직";
    }
}
