package com.seproject.oauth2.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class AuthorizationMetaData {

    @GeneratedValue @Id
    private Long id;

    @Column(name="method_signature",unique = true)
    private String methodSignature;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    public boolean matches(List<String> roles) {
        boolean flag = false;
        for (String role : roles) {
            flag |= role.equals(this.role.getAuthority());
        }

        return flag;
    }
}
