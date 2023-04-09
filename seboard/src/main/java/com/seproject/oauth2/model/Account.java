package com.seproject.oauth2.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts"
        ,uniqueConstraints = {@UniqueConstraint(name="Account's loginId is unique",columnNames="loginId")})
public class Account {

    @Id @GeneratedValue
    private Long accountId;
    private String loginId;
    private String registrationId;
    private String username;
    private String password;
    private String provider;
    private String email;
    private String profile;

    @ManyToMany
    @JoinTable(
            name="authorities",
            joinColumns={@JoinColumn(name="account_id", referencedColumnName="accountId")},
            inverseJoinColumns={@JoinColumn(name="role_id", referencedColumnName="roleId")})
    private List<Role> authorities;
}
