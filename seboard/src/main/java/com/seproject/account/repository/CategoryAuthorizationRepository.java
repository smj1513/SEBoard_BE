package com.seproject.account.repository;

import com.seproject.account.model.CategoryAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryAuthorizationRepository extends JpaRepository<CategoryAuthorization,Long> {


    @Query("select data from CategoryAuthorization data where data.menu = :categoryId")
    List<CategoryAuthorization> findByCategoryId(Long categoryId);

}
