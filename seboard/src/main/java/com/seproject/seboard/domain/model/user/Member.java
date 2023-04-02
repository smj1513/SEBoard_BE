package com.seproject.seboard.domain.model.user;

import com.seproject.seboard.oauth2.model.Account;
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
public class Member extends BoardUser{
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
}
