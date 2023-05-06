package com.seproject.seboard.domain.model.post.exposeOptions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PUBLIC")
public class Public extends ExposeOption {

    @Override
    public ExposeState getExposeState() {
        return ExposeState.PUBLIC;
    }
}
