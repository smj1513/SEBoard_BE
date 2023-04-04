package com.seproject.seboard.domain.model.post;

import com.seproject.seboard.domain.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Builder
@Table(name = "categories")
public class Category {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 20;

    @Id @GeneratedValue
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "super_category_id")
    private Category superCategory;
    private String name;

    public Category(Long categoryId, Category superCategory, String name) {
        if(isValidName(name)){
            throw new IllegalArgumentException();
        }

        this.categoryId = categoryId;
        this.superCategory = superCategory;
        this.name = name;
    }

    public boolean isRemovable(CategoryService categoryService){
        return categoryService.hasPost(categoryId);
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
