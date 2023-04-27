package com.seproject.seboard.domain.model.exposeOptions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "kumohs")
public class Kumoh extends ExposeOption{
    public Kumoh() {
        super(ExposeState.KUMOH);
    }
}
