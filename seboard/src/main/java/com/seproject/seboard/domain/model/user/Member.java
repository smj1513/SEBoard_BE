package com.seproject.seboard.domain.model.user;

import com.seproject.oauth2.model.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="members")
@PrimaryKeyJoinColumn(name="member_id")
public class Member extends BoardUser{

    @JoinColumn(name="account_id")
    @OneToOne
    private Account account;


    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Override
    public boolean isOwnAccountId(Long accountId) {
        return account.getAccountId().equals(accountId);
    }

    @Override
    public String getLoginId() {
        return account.getLoginId();
    }
}
