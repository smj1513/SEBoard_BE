package com.seproject.seboard.application;

import com.seproject.seboard.application.dto.category.CategoryCommand.CategoryCreateCommand;
import com.seproject.seboard.application.dto.category.CategoryCommand.CategoryUpdateCommand;
import com.seproject.seboard.domain.model.category.BoardMenu;
import com.seproject.seboard.domain.model.category.Category;
import com.seproject.seboard.domain.model.category.ExternalSiteMenu;
import com.seproject.seboard.domain.model.category.Menu;
import com.seproject.seboard.domain.repository.category.BoardMenuRepository;
import com.seproject.seboard.domain.repository.category.CategoryRepository;
import com.seproject.seboard.domain.repository.category.ExternalSiteMenuRepository;
import com.seproject.seboard.domain.repository.category.MenuRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.seproject.seboard.application.utils.AppServiceHelper.findByIdOrThrow;

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

    public void createCategory(CategoryCreateCommand command){
        //TODO : 상위 카테고리로 지정할 수 있는 카테고리 구분?
        Menu superMenu = menuRepository.findById(command.getSuperCategoryId()).get();

        if(command.getCategoryType().equals("MENU")){
            //TODO : urlInfo NULL일 경우 자동으로 넣는 로직 필요
            if(superMenu!=null){
                throw new IllegalArgumentException();
            }

            Menu menu = Menu.builder()
                    .superMenu(null)
                    .name(command.getName())
                    .description(command.getDescription())
                    .urlInfo(command.getUrlId())
                    .build();

            menuRepository.save(menu);
        }else if(command.getCategoryType().equals("BOARD")){
            BoardMenu boardMenu = BoardMenu.builder()
                    .superMenu(superMenu)
                    .name(command.getName())
                    .description(command.getDescription())
                    .urlInfo(command.getUrlId())
                    .build();

            boardMenuRepository.save(boardMenu);

            Category category = Category.builder()
                    .superMenu(boardMenu)
                    .name("일반")
                    .description("기본으로 생성되는 게시판 카테고리")
                    .urlInfo(null)
                    .build();

            categoryRepository.save(category);
        }else if(command.getCategoryType().equals("CATEGORY")){
            Category category = Category.builder()
                    .superMenu(superMenu)
                    .name(command.getName())
                    .description(command.getDescription())
                    .urlInfo(command.getUrlId())
                    .build();

            categoryRepository.save(category);
        }else if(command.getCategoryType().equals("EXTERNAL")){
            ExternalSiteMenu externalSiteCategory = ExternalSiteMenu.builder()
                    .superMenu(superMenu)
                    .name(command.getName())
                    .description(command.getDescription())
                    .urlInfo(command.getExternalUrl())
                    .build();

            externalSiteMenuRepository.save(externalSiteCategory);
        }else{
            throw new IllegalArgumentException();
        }
    }

    public void updateCategory(CategoryUpdateCommand command){
        //TODO : superCategory 수정?
        Menu targetMenu = findByIdOrThrow(command.getCategoryId(), categoryRepository, "");

        targetMenu.changeName(command.getName());
        targetMenu.changeName(command.getDescription());

        if(targetMenu instanceof Menu) {
            ((Menu) targetMenu).changeCategoryPathId(command.getUrlId());
        }else if(targetMenu instanceof com.seproject.seboard.domain.model.category.ExternalSiteMenu) {
            ((com.seproject.seboard.domain.model.category.ExternalSiteMenu) targetMenu).changeExternalSiteUrl(command.getExternalUrl());
        }
    }


    public void deleteCategory(Long categoryId){
        Menu targetMenu = findByIdOrThrow(categoryId, categoryRepository, "");

        //TODO : message
        if(!targetMenu.isRemovable(categoryService)){
            throw new IllegalArgumentException();
        }

        categoryRepository.deleteById(categoryId);
    }

    public void migrateCategory(Long fromCategoryId, Long toCategoryId){
        //TODO : 권한 처리 어디서?
        Menu from = findByIdOrThrow(fromCategoryId, categoryRepository, "");
        Menu to = findByIdOrThrow(toCategoryId, categoryRepository, "");

        //TODO : bulk update 적용
        postRepository.findByCategoryId(fromCategoryId).forEach(post -> {
            post.changeCategory(to);
        });
    }

//    public List<CategoryDTO.CategoryResponseDTO> retrieveCategoryList(){
//        //TODO : paging 고려
//        return categoryRepository.findAll().stream().map(
//                CategoryDTO.CategoryResponseDTO::toDTO
//        ).collect(Collectors.toList());
//    }
//
}
