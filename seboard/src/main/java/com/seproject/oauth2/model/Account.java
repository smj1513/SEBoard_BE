package com.seproject.oauth2.model;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts"
        ,uniqueConstraints = {@UniqueConstraint(name="Account's loginId is unique",columnNames="loginId")})
public class Account implements UserDetails {

    @Id @GeneratedValue
    private Long accountId;
    private String loginId;
    private String username;
    private String nickname;
    private String password;
    private String provider;
    private String email;
    private String profile;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="authorities",
            joinColumns={@JoinColumn(name="account_id", referencedColumnName="accountId")},
            inverseJoinColumns={@JoinColumn(name="role_id", referencedColumnName="roleId")})
    private List<Role> authorities;

    public void updateProfile(String profile) {
        this.profile = profile;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
