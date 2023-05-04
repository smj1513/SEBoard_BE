package com.seproject.seboard.domain.model.category;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "board_categories")
@NoArgsConstructor
@SuperBuilder
public class BoardCategory extends Category{
    private String categoryPathId;

    protected BoardCategory(Long categoryId, Category superCategory, String name, String description, String categoryPathId) {
        super(categoryId, superCategory, name, description);
        this.categoryPathId = categoryPathId;

        if(categoryPathId==null || categoryPathId.isEmpty()){
            this.categoryPathId = RandomStringUtils.randomAlphabetic(10);
        }
    }

    public void changeCategoryPathId(String urlId) {
        this.categoryPathId = urlId;
    }
}
