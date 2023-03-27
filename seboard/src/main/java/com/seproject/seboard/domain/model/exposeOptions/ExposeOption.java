package com.seproject.seboard.domain.model.exposeOptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn
@Inheritance(strategy= InheritanceType.JOINED)
@Entity
@Table(name="expose_options")
public abstract class ExposeOption {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ExposeState exposeState;
}
