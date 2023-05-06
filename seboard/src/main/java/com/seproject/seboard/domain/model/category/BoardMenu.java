package com.seproject.seboard.domain.model.category;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuperBuilder
@Entity
@NoArgsConstructor
@DiscriminatorValue("BOARD")
public class BoardMenu extends InternalSiteMenu {

    public BoardMenu(Long categoryId, Menu superMenu, String name, String description, String categoryPathId) {
        super(categoryId, superMenu, name, description, categoryPathId);

        if(getDepth() > 2){
            throw new IllegalArgumentException();
        }
    }
}
