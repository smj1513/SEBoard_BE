package com.seproject.board.menu.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.role.domain.Role;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.board.menu.domain.*;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.board.menu.application.dto.CategoryCommand.CategoryUpdateCommand;
import com.seproject.board.menu.controller.dto.CategoryResponse;
import com.seproject.board.menu.domain.repository.BoardMenuRepository;
import com.seproject.board.menu.domain.repository.CategoryRepository;
import com.seproject.board.menu.domain.repository.ExternalSiteMenuRepository;
import com.seproject.board.menu.domain.repository.MenuRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.board.menu.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.seproject.board.common.utils.AppServiceHelper.findByIdOrThrow;
import static com.seproject.board.menu.controller.dto.CategoryDTO.*;
import static com.seproject.admin.authorization.controller.dto.AuthorizationDTO.*;

@Service
@AllArgsConstructor
@Transactional
public class CategoryAppService {
    private final CategoryService categoryService;
    private final MenuRepository menuRepository;
    private final BoardMenuRepository boardMenuRepository;
    private final CategoryRepository categoryRepository;
    private final ExternalSiteMenuRepository externalSiteMenuRepository;
    private final PostRepository postRepository;

    public void updateCategory(CategoryUpdateCommand command){
        //TODO : superCategory 수정? -> 여기 categoryRepository가 맞나요 menuRepository가 맞나요
        Menu targetMenu = findByIdOrThrow(command.getCategoryId(), menuRepository, "");

        targetMenu.changeName(command.getName());

        if(targetMenu instanceof InternalSiteMenu){
            InternalSiteMenu internalSiteMenu = (InternalSiteMenu) targetMenu;
            internalSiteMenu.changeCategoryPathId(command.getUrlId());
        }else if(targetMenu instanceof ExternalSiteMenu){
            ExternalSiteMenu externalSiteMenu = (ExternalSiteMenu) targetMenu;
            externalSiteMenu.changeExternalSiteUrl(command.getExternalUrl());
        }
    }


    public void deleteCategory(Long categoryId){
        Menu targetMenu = findByIdOrThrow(categoryId, categoryRepository, "");

        //TODO : message
        if(!targetMenu.isRemovable(categoryService)){
            throw new CustomIllegalArgumentException(ErrorCode.CANNOT_DELETE_MENU,null);
        }

        categoryRepository.deleteById(categoryId);
    }

    public void migrateCategory(Long fromMenuId, Long toMenuId){
        BoardMenu from = boardMenuRepository.findById(fromMenuId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MENU));
        BoardMenu to = boardMenuRepository.findById(toMenuId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MENU));

        //TODO : bulk update 적용
        categoryRepository.findBySuperMenu(from).stream()
                .forEach(category -> category.changeSuperMenu(to));
    }

    public CategoryResponse retrieveMenuById(Long menuId){
        Menu targetMenu = menuRepository.findById(menuId).orElseThrow();
        CategoryResponse res = new CategoryResponse(targetMenu);

        retrieveSubMenu(targetMenu, res);

        return res;
    }

    protected void retrieveSubMenu(Menu targetMenu, CategoryResponse res){
        if(targetMenu.getDepth()==0){
            List<Menu> depth1menu = menuRepository.findBySuperMenu(targetMenu.getMenuId());
            for(Menu menu : depth1menu){
                CategoryResponse subMenuRes = new CategoryResponse(menu);
                List<Menu> depth2menu = menuRepository.findBySuperMenu(menu.getMenuId());
                for(Menu menu2 : depth2menu){
                    subMenuRes.addSubMenu(new CategoryResponse(menu2));
                }
                res.addSubMenu(subMenuRes);
            }
        }else if(targetMenu.getDepth()==1){
            List<Menu> depth2menu = menuRepository.findBySuperMenu(targetMenu.getMenuId());
            for(Menu menu : depth2menu){
                res.addSubMenu(new CategoryResponse(menu));
            }
        }
    }

    public List<CategoryResponse> retrieveAllMenu() {

        Optional<Account> account = SecurityUtils.getAccount();
        List<Role> roles = account.isPresent() ? account.get().getRoles() : List.of();

        List<Menu> menus = menuRepository.findByDepth(0);
        Map<Menu,List<Menu>> menuHierarchical = new HashMap<>();

        for (Menu menu : menus) {
            if(menu.exposable(roles)) {
                List<Menu> subMenus = menuRepository.findBySuperMenu(menu.getMenuId());
                menuHierarchical.put(menu,subMenus);
            }
        }

        List<CategoryResponse> categoryResponses = new ArrayList<>();

        menuHierarchical.forEach((menu, subMenus) -> {
            CategoryResponse categoryResponse = new CategoryResponse(menu);
            subMenus.stream()
                    .map(CategoryResponse::new)
                    .forEach(categoryResponse::addSubMenu);

            categoryResponses.add(categoryResponse);
        });

        return categoryResponses;
    }

    public Map<Menu,List<Menu>> retrieveAllMenuForAdmin() {
        List<Menu> menus = menuRepository.findByDepth(0);
        Map<Menu,List<Menu>> response = new HashMap<>();

        for (Menu menu : menus) {
            List<Menu> subMenus = Collections.emptyList();
            if(!(menu instanceof BoardMenu)) {
                subMenus = menuRepository.findBySuperMenu(menu.getMenuId());
            }
            response.put(menu,subMenus);
        }

        return response;
    }

}
