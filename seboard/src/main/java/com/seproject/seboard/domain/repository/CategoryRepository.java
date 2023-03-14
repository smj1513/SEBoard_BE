package com.seproject.seboard.domain.repository;

import com.seproject.seboard.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
