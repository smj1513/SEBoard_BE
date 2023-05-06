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
@Table(name = "menus")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "menu_type")
@DiscriminatorValue("MENU")
public class Menu {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 20;

    @Id @GeneratedValue
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "super_menu_id")
    private Menu superMenu;
    private String name;
    private String description;
    private int depth;
    protected String urlInfo;

    public Menu(Long menuId, Menu superMenu, String name, String description) {
        if(isValidName(name)){
            throw new IllegalArgumentException();
        }

        this.menuId = menuId;
        this.superMenu = superMenu;
        this.name = name;
        this.description = description;
        this.depth = calculateDepth();

        if(depth > 0){
            throw new IllegalArgumentException();
        }
    }

    protected int calculateDepth() {
        if(superMenu == null){
            return 0;
        }else{
            return superMenu.getDepth() + 1;
        }
    }

    public boolean isRemovable(CategoryService categoryService){
        return !categoryService.hasSubCategory(menuId);
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

    public void changeDescription(String description) {
        this.description = description;
    }
}
