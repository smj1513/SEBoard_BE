package com.seproject.account.account.domain;

import com.seproject.account.role.domain.Role;
import com.seproject.board.common.Status;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "FORM_ACCOUNT")
@Table(name = "form_accounts")
public class FormAccount extends Account {

    @Builder
    public FormAccount(Long accountId, String loginId,
                   String name, String nickname,
                   String password, List<Role> authorities) {
        this.accountId = accountId;
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.authorities = authorities;
        this.createdAt = LocalDateTime.now();
        this.status = Status.NORMAL;
    }
}
