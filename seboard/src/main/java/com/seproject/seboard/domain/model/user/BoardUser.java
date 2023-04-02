package com.seproject.seboard.domain.model.user;



import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "board_users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BoardUser {
    @Id @GeneratedValue
    @Column(name = "board_user_id")
    protected Long boardUserId;

    public abstract boolean isAnonymous();

    public abstract boolean isOwnAccountId(Long accountId);
}
