package com.seproject.seboard.domain.model.category;

import com.seproject.admin.domain.AccessOption;
import com.seproject.admin.domain.MenuAuthorization;
import com.seproject.seboard.domain.service.CategoryService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    @Builder.Default
    @OneToMany(mappedBy = "menu",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<MenuAuthorization> menuAuthorizations = new ArrayList<>();

    public Menu(Long menuId, Menu superMenu, String name, String description) {
        if(isValidName(name)){
            throw new IllegalArgumentException();
        }

        this.menuId = menuId;
        this.superMenu = superMenu;
        this.name = name;
        this.description = description;
        this.depth = calculateDepth();
        this.menuAuthorizations = new ArrayList<>();
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

    public boolean isRootMenu() {
        return depth == 0;
    }

    public boolean isRemovable(CategoryService categoryService){
        return !categoryService.hasSubCategory(menuId);
    }

    public void changeName(String name) {
//        if(isValidName(name)){
//            throw new IllegalArgumentException();
//        }

        this.name = name;
    }

    private boolean isValidName(String name) {
        return MIN_NAME_LENGTH < name.length() && name.length() < MAX_NAME_LENGTH;
    }

    public void updateMenuAuthorizations(List<MenuAuthorization> menuAuthorizations) {
        this.menuAuthorizations.addAll(menuAuthorizations);
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeSuperMenu(Menu superMenu) {
        this.superMenu = superMenu;
    }

    public boolean writable(Collection<? extends GrantedAuthority> authorities) {
        List<MenuAuthorization> collect = menuAuthorizations.stream()
                .filter((menuAuthorization) -> menuAuthorization.getAccessOption().equals(AccessOption.WRITE))
                .collect(Collectors.toList());
        if(collect.size() == 0) return true;
        boolean flag = false;

        for (MenuAuthorization menuAuthorization : collect) {
            flag |= menuAuthorization.writable(authorities);
        }

        return flag;
    }

    public boolean manageable(Collection<? extends GrantedAuthority> authorities) {
        List<MenuAuthorization> collect = menuAuthorizations.stream()
                .filter((menuAuthorization) -> menuAuthorization.getAccessOption().equals(AccessOption.MANAGE))
                .collect(Collectors.toList());
        if(collect.size() == 0) return true;
        boolean flag = false;

        for (MenuAuthorization menuAuthorization : collect) {
            flag |= menuAuthorization.manageable(authorities);
        }

        return flag;
    }

    public boolean exposable(Collection<? extends GrantedAuthority> authorities) {
        List<MenuAuthorization> collect = menuAuthorizations.stream()
                .filter((menuAuthorization) -> menuAuthorization.getAccessOption().equals(AccessOption.EXPOSE))
                .collect(Collectors.toList());
        if(collect.size() == 0) return true;
        boolean flag = false;

        for (MenuAuthorization menuAuthorization : menuAuthorizations) {
            flag |= menuAuthorization.exposable(authorities);
        }

        return flag;
    }
}
