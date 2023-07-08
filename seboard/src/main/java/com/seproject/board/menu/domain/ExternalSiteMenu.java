package com.seproject.board.menu.domain;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("EXTERNAL")
public class ExternalSiteMenu extends Menu {

    public ExternalSiteMenu(Long menuId, Menu superMenu, String name, String description) {
        super(menuId, superMenu, name, description);

        if(getDepth()>2){
            throw new IllegalArgumentException();
        }
    }

    public void changeExternalSiteUrl(String externalUrl) {
        this.urlInfo = externalUrl;
    }
}
