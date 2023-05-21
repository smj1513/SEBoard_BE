package com.seproject.account.repository.role.auth;

import com.seproject.account.model.role.auth.CategoryAuthorization;
import com.seproject.account.model.role.auth.CategoryManagerAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryManagerAuthorizationRepository extends JpaRepository<CategoryManagerAuthorization,Long> {

    @Query("select c from CategoryManagerAuthorization c where c.category.menuId = :categoryId")
    List<CategoryManagerAuthorization> findByCategoryId(Long categoryId);
}
