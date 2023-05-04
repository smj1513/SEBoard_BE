package com.seproject.seboard.domain.repository.category;

import com.seproject.seboard.domain.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select case when count(c)>0 then true else false end from Category c where c.superCategory.categoryId = :categoryId")
    boolean existsSubCategory(Long categoryId);
}
