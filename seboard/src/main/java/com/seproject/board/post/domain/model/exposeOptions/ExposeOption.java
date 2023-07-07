package com.seproject.board.post.domain.model.exposeOptions;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "expose_options")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "expose_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ExposeOption {
    @Id @GeneratedValue
    private Long exposeOptionId;
    @Enumerated(EnumType.STRING)
    @Column(name = "expose_type", insertable = false, updatable = false)
    protected ExposeState exposeState;
    protected String password;

    public abstract ExposeState getExposeState();


    public static ExposeOption of(ExposeState state, String password){
        switch (state){
            case PUBLIC:
                return new Public();
            case KUMOH:
                return new Kumoh();
            case PRIVACY:
                return new Privacy(password);
            default:
                throw new IllegalArgumentException("Invalid ExposeState");
        }
    }

}
