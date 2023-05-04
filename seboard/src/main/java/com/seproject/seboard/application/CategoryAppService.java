package com.seproject.seboard.application;

import com.seproject.seboard.application.dto.category.CategoryCommand;
import com.seproject.seboard.application.dto.category.CategoryCommand.CategoryCreateCommand;
import com.seproject.seboard.application.dto.category.CategoryCommand.CategoryUpdateCommand;
import com.seproject.seboard.domain.model.category.BoardCategory;
import com.seproject.seboard.domain.model.category.Category;
import com.seproject.seboard.domain.model.category.ExternalSiteCategory;
import com.seproject.seboard.domain.repository.category.BoardCategoryRepository;
import com.seproject.seboard.domain.repository.category.CategoryRepository;
import com.seproject.seboard.domain.repository.category.ExternalSiteCategoryRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.service.CategoryService;
import com.seproject.account.model.Account;
import com.seproject.account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.seproject.seboard.application.utils.AppServiceHelper.findByIdOrThrow;

@Service
@AllArgsConstructor
@Transactional
public class CategoryAppService {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final ExternalSiteCategoryRepository externalSiteCategoryRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    public void createCategory(CategoryCreateCommand command){
        //TODO : 상위 카테고리로 지정할 수 있는 카테고리 구분?
        Category superCategory = findByIdOrThrow(command.getSuperCategoryId(), categoryRepository, "");

        if(command.getCategoryType().equals("BOARD")){
            BoardCategory boardCategory = BoardCategory.builder()
                    .superCategory(superCategory)
                    .name(command.getName())
                    .description(command.getDescription())
                    .categoryPathId(command.getUrlId())
                    .build();

            boardCategoryRepository.save(boardCategory);
        }else if(command.getCategoryType().equals("EXTERNAL")){
            ExternalSiteCategory externalSiteCategory = ExternalSiteCategory.builder()
                    .superCategory(superCategory)
                    .name(command.getName())
                    .description(command.getDescription())
                    .externSiteUrl(command.getExternalUrl())
                    .build();

            externalSiteCategoryRepository.save(externalSiteCategory);
        }else{
            throw new IllegalArgumentException();
        }
    }

    public void updateCategory(CategoryUpdateCommand command){
        //TODO : superCategory 수정?
        Category targetCategory = findByIdOrThrow(command.getCategoryId(), categoryRepository, "");

        targetCategory.changeName(command.getName());
        targetCategory.changeName(command.getDescription());

        if(targetCategory instanceof BoardCategory) {
            ((BoardCategory) targetCategory).changeCategoryPathId(command.getUrlId());
        }else if(targetCategory instanceof ExternalSiteCategory) {
            ((ExternalSiteCategory) targetCategory).changeExternalSiteUrl(command.getExternalUrl());
        }
    }


    public void deleteCategory(Long categoryId){
        Category targetCategory = findByIdOrThrow(categoryId, categoryRepository, "");

        //TODO : message
        if(!targetCategory.isRemovable(categoryService)){
            throw new IllegalArgumentException();
        }

        categoryRepository.deleteById(categoryId);
    }

    public void migrateCategory(Long fromCategoryId, Long toCategoryId){
        //TODO : 권한 처리 어디서?
        Category from = findByIdOrThrow(fromCategoryId, categoryRepository, "");
        Category to = findByIdOrThrow(toCategoryId, categoryRepository, "");

        //TODO : bulk update 적용
        postRepository.findByCategoryId(fromCategoryId).forEach(post -> {
            post.changeCategory(to);
        });
    }
//
//    public List<CategoryDTO.CategoryResponseDTO> retrieveCategoryList(){
//        //TODO : paging 고려
//        return categoryRepository.findAll().stream().map(
//                CategoryDTO.CategoryResponseDTO::toDTO
//        ).collect(Collectors.toList());
//    }
//
//    private <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repo, String errorMsg){
//        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(errorMsg));
//    }
}
