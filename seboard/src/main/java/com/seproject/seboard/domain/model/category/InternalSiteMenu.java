package com.seproject.seboard.domain.model.category;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@SuperBuilder
@NoArgsConstructor
public abstract class InternalSiteMenu extends Menu {

    protected InternalSiteMenu(Long categoryId, Menu superMenu, String name, String description, String categoryPathId) {
        super(categoryId, superMenu, name, description);

        this.urlInfo = categoryPathId;

        if(categoryPathId==null || categoryPathId.isEmpty()){
            this.urlInfo = RandomStringUtils.randomAlphabetic(10);
        }
    }

    public void changeCategoryPathId(String urlId) {
        this.urlInfo = urlId;
    }
}
