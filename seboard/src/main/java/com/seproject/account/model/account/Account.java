package com.seproject.account.model.account;

import com.seproject.account.model.role.Role;
import com.seproject.seboard.domain.model.common.Status;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "accounts")
public abstract class Account implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long accountId;

    protected String loginId;
    protected String name;
    protected String nickname;
    protected String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="authorities",
            joinColumns={@JoinColumn(name="account_id", referencedColumnName="accountId")},
            inverseJoinColumns={@JoinColumn(name="role_id", referencedColumnName="roleId")})
    protected List<Role> authorities;
    protected LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    protected Status status;

//    @Builder
//    public Account(Long accountId, String loginId,
//                   String name, String nickname,
//                   String password, List<Role> authorities) {
//        this.accountId = accountId;
//        this.loginId = loginId;
//        this.name = name;
//        this.nickname = nickname;
//        this.password = password;
//        this.authorities = authorities;
//        this.createdAt = LocalDateTime.now();
//        this.isDeleted = false;
//    }

    public Account update(String loginId,String password,String name,String nickname,List<Role> authorities) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.authorities = authorities;

        return this;
    }

    public String changePassword(String password) {
        this.password = password;
        return password;
    }

    public void delete(boolean isPermanent) {
        if(isPermanent) {
            status = Status.PERMANENT_DELETED;
        } else {
            status = Status.TEMP_DELETED;
        }
    }

    public String getUsername() {
        return loginId;
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
