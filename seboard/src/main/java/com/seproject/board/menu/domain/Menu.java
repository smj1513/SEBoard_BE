package com.seproject.board.menu.domain;

import com.seproject.account.authorization.utils.AuthorizationProperty;
import com.seproject.account.role.domain.Role;
import com.seproject.account.authorization.domain.MenuAuthorization;
import com.seproject.board.menu.service.MenuService;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
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

    //TODO : Internal -> urlId, External -> urlInfo
    protected String urlInfo;

    @OneToMany(mappedBy = "menu",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MenuAuthorization> menuAuthorizations = new ArrayList<>();

    public Menu(Long menuId, Menu superMenu, String name, String description) {
        if(!isValidName(name)){
            throw new IllegalArgumentException();
        }

        this.menuId = menuId;
        this.superMenu = superMenu;
        this.name = name;
        this.description = description;
        this.depth = calculateDepth();
        this.menuAuthorizations = new ArrayList<>();
    }

    public void addAuthorization(MenuAuthorization menuAuthorization) {
        this.menuAuthorizations.add(menuAuthorization);
        menuAuthorization.setMenu(this);
    }

    protected int calculateDepth() {
        if(superMenu == null){
            return 0;
        }else{
            return superMenu.getDepth() + 1;
        }
    }

    public boolean isRemovable(MenuService categoryService){
        return !categoryService.hasSubCategory(menuId);
    }

    public void changeName(String name) {
        if(!isValidName(name)){
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    private boolean isValidName(String name) {
        return MIN_NAME_LENGTH <= name.length() && name.length() <= MAX_NAME_LENGTH;
    }

    public void updateMenuAuthorizations(List<MenuAuthorization> menuAuthorizations) {
        this.menuAuthorizations.clear();
        this.menuAuthorizations.addAll(menuAuthorizations);
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeSuperMenu(Menu superMenu) {
        this.superMenu = superMenu;
    }

    public void changeUrlInfo(String urlInfo) {this.urlInfo = urlInfo;}


    //TODO : throw vs return true
    public boolean editable(List<Role> roles) {
        if (superMenu != null && !superMenu.editable(roles)) {
            return false;
        }

        for (MenuAuthorization menuAuthorization : menuAuthorizations) {
            if(menuAuthorization.support(AuthorizationProperty.EDITABLE)) {
                return menuAuthorization.isAuth(roles);
            }
        }
        return true;
    }

    public boolean manageable(List<Role> roles) {
        if (superMenu != null && !superMenu.manageable(roles)) {
            return false;
        }

        for (MenuAuthorization menuAuthorization : menuAuthorizations) {
            if(menuAuthorization.support(AuthorizationProperty.MANAGEABLE)) {
                return menuAuthorization.isAuth(roles);
            }
        }
        return true;
    }

    public boolean accessible(List<Role> roles) {

        if (superMenu != null && !superMenu.accessible(roles)) {
            return false;
        }

        for (MenuAuthorization menuAuthorization : menuAuthorizations) {
            if(menuAuthorization.support(AuthorizationProperty.ACCESS)) {
                return menuAuthorization.isAuth(roles);
            }
        }
        return true;
    }

    public boolean exposable(List<Role> roles) {

        if (superMenu != null && !superMenu.exposable(roles)) {
            return false;
        }

        for (MenuAuthorization menuAuthorization : menuAuthorizations) {
            if(menuAuthorization.support(AuthorizationProperty.EXPOSE)) {
                return menuAuthorization.isAuth(roles);
            }
        }
        return true;
    }
}
