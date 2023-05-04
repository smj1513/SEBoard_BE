package com.seproject.seboard.domain.model.category;

import com.seproject.seboard.domain.service.CategoryService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@SuperBuilder
@Table(name = "categories")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Category {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 20;

    @Id @GeneratedValue
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "super_category_id")
    private Category superCategory;
    private String name;
    private String description;

    public Category(Long categoryId, Category superCategory, String name, String description) {
        if(isValidName(name)){
            throw new IllegalArgumentException();
        }

        this.categoryId = categoryId;
        this.superCategory = superCategory;
        this.name = name;
        this.description = description;
    }

    public boolean isRemovable(CategoryService categoryService){
        return !categoryService.hasPost(categoryId) && !categoryService.hasSubCategory(categoryId);
    }

    public void changeName(String name) {
        if(isValidName(name)){
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    private boolean isValidName(String name) {
        return MIN_NAME_LENGTH < name.length() && name.length() < MAX_NAME_LENGTH;
    }
}
