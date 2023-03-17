package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.Author;
import com.seproject.seboard.domain.model.Category;
import com.seproject.seboard.domain.repository.AuthorRepository;
import com.seproject.seboard.domain.repository.CategoryRepository;
import com.seproject.seboard.domain.service.CategoryService;
import com.seproject.seboard.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CategoryAppService {
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final CategoryService categoryService;

    public void createCategory(Long superCategoryId, Long userId, String name){
        Author requestUser = findByIdOrThrow(userId, authorRepository, "");
        Category superCategory = findByIdOrThrow(superCategoryId, categoryRepository, "");
        //TODO : 권한 처리

        //TODO : category name에 대한 validation?
        Category createdCategory = Category.builder()
                .superCategory(superCategory)
                .name(name)
                .build();

        categoryRepository.save(createdCategory);
    }

    //update? changeName? update는 너무 전체적으로 변경하는 느낌 아닌가?
    public void updateCategory(Long categoryId, Long userId, String name){
        Author requestUser = findByIdOrThrow(userId, authorRepository, "");
        //TODO : 권한 처리 어디서? post update는?
        Category targetCategory = findByIdOrThrow(categoryId, categoryRepository, "");

        targetCategory.changeName(name);
    }

    public void removeCategory(Long categoryId, Long userId){
        Author requestUser = findByIdOrThrow(userId, authorRepository, "");
        //TODO : 권한 처리 어디서?
        Category targetCategory = findByIdOrThrow(categoryId, categoryRepository, "");

        //TODO : message
        if(!categoryService.isRemovable(categoryId)){
            throw new IllegalArgumentException();
        }

        categoryRepository.deleteById(categoryId);
    }

    public void migrateCategory(Long fromId, Long toId, Long userId){
        Author requestUser = findByIdOrThrow(userId, authorRepository, "");
        //TODO : 권한 처리 어디서?
        Category from = findByIdOrThrow(fromId, categoryRepository, "");
        Category to = findByIdOrThrow(toId, categoryRepository, "");

        //TODO : from에 있는 Post를 to로 카테고리 변경
    }

    public List<CategoryDTO.CategoryResponseDTO> retrieveCategoryList(){
        //TODO : paging 고려
        return categoryRepository.findAll().stream().map(
                CategoryDTO.CategoryResponseDTO::toDTO
        ).collect(Collectors.toList());
    }

    private <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repo, String errorMsg){
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(errorMsg));
    }
}
