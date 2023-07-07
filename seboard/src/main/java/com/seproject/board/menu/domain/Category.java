package com.seproject.board.menu.domain;

import com.seproject.board.menu.service.CategoryService;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Collection;

@SuperBuilder
@NoArgsConstructor
@Entity
@DiscriminatorValue("CATEGORY")
public class Category extends BoardMenu {
    public Category(Long categoryId, Menu superMenu, String name, String description, String categoryPathId) {
        super(categoryId, superMenu, name, description, categoryPathId);

        if(superMenu.getClass()!=BoardMenu.class){ //TODO : 이게 맞나?
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean exposable(Collection<? extends GrantedAuthority> authorities) {
        return getSuperMenu().exposable(authorities);
    }
    @Override
    public boolean isRemovable(CategoryService categoryService) {
        return !categoryService.hasPost(getMenuId());
    }
}
