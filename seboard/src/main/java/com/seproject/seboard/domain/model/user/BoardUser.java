package com.seproject.seboard.domain.model.user;



import javax.persistence.*;

@Entity
@Table(name = "board_users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BoardUser {
    @Id @GeneratedValue
    @Column(name = "board_user_id")
    private Long boardUserId;
}
