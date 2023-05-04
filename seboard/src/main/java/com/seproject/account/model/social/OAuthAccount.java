package com.seproject.account.model.social;

import com.seproject.account.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="oauth_accounts")
public class OAuthAccount {

    @Id @GeneratedValue
    private Long id;

    private String provider;

    @Column(unique = true)
    private String sub;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
