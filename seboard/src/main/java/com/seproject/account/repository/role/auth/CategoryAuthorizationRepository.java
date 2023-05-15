package com.seproject.account.repository.role.auth;

import com.seproject.account.model.role.auth.CategoryAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryAuthorizationRepository extends JpaRepository<CategoryAuthorization,Long> {

    @Query("select c from CategoryAuthorization c where c.category.menuId = :categoryId")
    List<CategoryAuthorization> findByCategoryId(Long categoryId);

    @Query("select c from CategoryAuthorization c where c.category.menuId = :categoryId and c.method = :method")
    CategoryAuthorization findByCategoryIdAndMethod(Long categoryId,String method);

    @Query("select c from CategoryAuthorization c where c.category.menuId = :categoryId and c.accessType != 'READ'")
    List<CategoryAuthorization> findByCategoryIdAndAccessType(Long categoryId);
}
