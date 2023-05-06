package com.seproject.seboard.domain.model.category;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("external_site")
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
