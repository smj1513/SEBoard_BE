package com.seproject.seboard.domain.model.exposeOptions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "publics")
public class Public extends ExposeOption {

    public Public() {
        super(ExposeState.PUBLIC);
    }

}
