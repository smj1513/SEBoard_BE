package com.seproject.seboard.domain.model.user;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(name = "board_users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BoardUser {
    @Id @GeneratedValue
    @Column(name = "board_user_id")
    protected Long boardUserId;
    protected String name;

    public abstract boolean isAnonymous();

    public abstract boolean isOwnAccountId(Long accountId);

    public abstract String getLoginId();
}
