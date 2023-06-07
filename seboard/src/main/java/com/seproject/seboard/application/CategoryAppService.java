package com.seproject.seboard.application;

import com.seproject.account.model.role.Role;
import com.seproject.admin.domain.AccessOption;
import com.seproject.admin.domain.SelectOption;
import com.seproject.admin.service.AdminMenuService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.application.dto.category.CategoryCommand.CategoryCreateCommand;
import com.seproject.seboard.application.dto.category.CategoryCommand.CategoryUpdateCommand;
import com.seproject.seboard.controller.dto.post.CategoryResponse;
import com.seproject.seboard.domain.model.category.*;
import com.seproject.seboard.domain.repository.category.BoardMenuRepository;
import com.seproject.seboard.domain.repository.category.CategoryRepository;
import com.seproject.seboard.domain.repository.category.ExternalSiteMenuRepository;
import com.seproject.seboard.domain.repository.category.MenuRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.seproject.seboard.application.utils.AppServiceHelper.findByIdOrThrow;
import static com.seproject.admin.controller.dto.CategoryDTO.*;
import static com.seproject.admin.dto.AuthorizationDTO.*;

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
    private final AdminMenuService adminMenuService;

    public void createCategory(CategoryCreateCommand command){
        //TODO : 상위 카테고리로 지정할 수 있는 카테고리 구분?
        //TODO : menuRepository Id 널포익
        Long superCategoryId = command.getSuperCategoryId();


        if(command.getCategoryType().equals("MENU")){
            //TODO : urlInfo NULL일 경우 자동으로 넣는 로직 필요
            if(superCategoryId != null){
                throw new IllegalArgumentException();
            }

            Menu menu = Menu.builder()
                    .superMenu(null)
                    .name(command.getName())
                    .description(command.getDescription())
                    .build();

            CategoryAccessUpdateRequestElement access = command.getAccess();

            if(access == null) {
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST, null);
            }

            adminMenuService.accessUpdate(menu,access);

            menuRepository.save(menu);
        }else if(command.getCategoryType().equals("BOARD")){
            Optional<Menu> superMenuOptional = menuRepository.findById(superCategoryId);
            Menu superMenu = superMenuOptional.orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY, null));
            BoardMenu boardMenu = BoardMenu.builder()
                    .superMenu(superMenu)
                    .name(command.getName())
                    .description(command.getDescription())
                    .urlInfo(command.getUrlId())
                    .build();

            CategoryAccessUpdateRequestElement expose = command.getExpose();
            CategoryAccessUpdateRequestElement access = command.getAccess();

            if(access == null || expose == null) {
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST, null);
            }

            boardMenuRepository.save(boardMenu);

            adminMenuService.accessUpdate(boardMenu,access);
            adminMenuService.update(boardMenu,expose,AccessOption.EXPOSE);

            Category category = Category.builder()
                    .superMenu(boardMenu)
                    .name("일반")
                    .description("기본으로 생성되는 게시판 카테고리")
                    .urlInfo(null)
                    .depth(boardMenu.getDepth()+1)
                    .build();

            categoryRepository.save(category);
        }else if(command.getCategoryType().equals("CATEGORY")){
            Optional<Menu> superMenuOptional = menuRepository.findById(superCategoryId);
            Menu superMenu = superMenuOptional.orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY, null));

            Category category = Category.builder()
                    .superMenu(superMenu)
                    .name(command.getName())
                    .description(command.getDescription())
                    .urlInfo(command.getUrlId())
                    .depth(superMenu.getDepth()+1)
                    .build();

            categoryRepository.save(category);

            CategoryAccessUpdateRequestElement manage = command.getManage();
            CategoryAccessUpdateRequestElement write = command.getWrite();

            if(manage == null || write == null) {
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST, null);
            }

            adminMenuService.update(category,manage,AccessOption.MANAGE);
            adminMenuService.update(category,write,AccessOption.WRITE);

        }else if(command.getCategoryType().equals("EXTERNAL")){
            if(superCategoryId != null) {
                throw new IllegalArgumentException();
            }
//            Optional<Menu> superMenuOptional = menuRepository.findById(superCategoryId);
//            Menu superMenu = superMenuOptional.orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY, null));

            ExternalSiteMenu externalSiteCategory = ExternalSiteMenu.builder()
                    .name(command.getName())
                    .description(command.getDescription())
                    .urlInfo(command.getExternalUrl())
                    .build();
            CategoryAccessUpdateRequestElement expose = command.getExpose();
            if(expose == null) {
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST,null);
            }

            externalSiteMenuRepository.save(externalSiteCategory);
            adminMenuService.update(externalSiteCategory,expose,AccessOption.EXPOSE);
        }else{
            throw new IllegalArgumentException();
        }
    }

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

    public CategoryRetrieveResponse retrieveCategoryBySuperCategoryId(Long superMenuId){
        Menu menu = menuRepository.findById(superMenuId).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY,null));
        List<Menu> subMenus = menuRepository.findBySuperMenuWithAuthorization(superMenuId);

        return CategoryRetrieveResponse.toDTO(menu,subMenus);
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
        //TODO : query로 최적화
        List<Menu> mainMenus = menuRepository.findByDepth(0);
        List<CategoryResponse> res = new ArrayList<>();

        for(Menu mainMenu : mainMenus){
            CategoryResponse mainMenuRes = new CategoryResponse(mainMenu);
            retrieveSubMenu(mainMenu, mainMenuRes);
//            List<Menu> subMenus = menuRepository.findByDepth(1);
//
//            for(Menu subMenu : subMenus){
//                CategoryResponse subMenuRes = new CategoryResponse(subMenu);
//                List<Menu> categories = menuRepository.findByDepth(2);
//
//                for(Menu category : categories){
//                    subMenuRes.addSubMenu(new CategoryResponse(category));
//                }
//
//                mainMenuRes.addSubMenu(subMenuRes);
//            }

            res.add(mainMenuRes);
        }

        return res;
    }

    public Map<Menu,List<Menu>> retrieveAllMenu(List<Role> roles) {
        List<Menu> menus = menuRepository.findByDepth(0);
        Map<Menu,List<Menu>> response = new HashMap<>();

        for (Menu menu : menus) {
            if(menu.exposable(roles)) {
                List<Menu> subMenus = menuRepository.findBySuperMenu(menu.getMenuId());
                response.put(menu,subMenus);
            }
        }

        return response;
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
