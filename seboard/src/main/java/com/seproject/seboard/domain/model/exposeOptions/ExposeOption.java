package com.seproject.seboard.domain.model.exposeOptions;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn
@Inheritance(strategy= InheritanceType.JOINED)
@Entity
@Table(name="expose_options")
@Getter
public abstract class ExposeOption {

    @Id @GeneratedValue
    protected Long exposeOptionId;

    @Enumerated(EnumType.STRING)
    protected ExposeState exposeState;

    protected ExposeOption(ExposeState state){
        this.exposeState = state;
    }

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
