package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.post.Category;
import com.seproject.seboard.domain.repository.post.CategoryRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.service.CategoryService;
import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.seproject.seboard.application.utils.AppServiceHelper.findByIdOrThrow;

@Service
@AllArgsConstructor
public class CategoryAppService {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    public void createCategory(Long superCategoryId, Long accountId, String name){
        Account requestAccount = findByIdOrThrow(accountId, accountRepository, "");
        Category superCategory = findByIdOrThrow(superCategoryId, categoryRepository, "");
        //TODO : 권한 처리

        //TODO : category name에 대한 validation?
        Category createdCategory = Category.builder()
                .superCategory(superCategory)
                .name(name)
                .build();

        categoryRepository.save(createdCategory);
    }

    public void updateCategory(Long categoryId, Long accountId, String name){
        Account requestAccount = findByIdOrThrow(accountId, accountRepository, "");
        //TODO : 권한 처리 어디서? post update는?
        //TODO : superCategory 수정?
        Category targetCategory = findByIdOrThrow(categoryId, categoryRepository, "");

        targetCategory.changeName(name);
    }


    public void deleteCategory(Long categoryId, Long accountId){
        Account requestAccount = findByIdOrThrow(accountId, accountRepository, "");

        //TODO : 권한 처리 어디서?
        Category targetCategory = findByIdOrThrow(categoryId, categoryRepository, "");

        //TODO : message
        if(!targetCategory.isRemovable(categoryService)){
            throw new IllegalArgumentException();
        }

        categoryRepository.deleteById(categoryId);
    }

    public void migrateCategory(Long fromId, Long toId, Long accountId){
        Account requestAccount = findByIdOrThrow(accountId, accountRepository, "");

        //TODO : 권한 처리 어디서?
        Category from = findByIdOrThrow(fromId, categoryRepository, "");
        Category to = findByIdOrThrow(toId, categoryRepository, "");

        //TODO : from에 있는 Post를 to로 카테고리 변경
        //TODO : JPQL?
        postRepository.findByCategoryId(fromId).forEach(post -> {
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
