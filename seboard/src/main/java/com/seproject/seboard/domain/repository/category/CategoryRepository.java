package com.seproject.seboard.domain.repository.category;

import com.seproject.seboard.domain.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
