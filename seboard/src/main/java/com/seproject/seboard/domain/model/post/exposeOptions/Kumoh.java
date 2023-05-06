package com.seproject.seboard.domain.model.post.exposeOptions;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("KUMOH")
public class Kumoh extends ExposeOption{
    @Override
    public ExposeState getExposeState() {
        return ExposeState.KUMOH;
    }
}
