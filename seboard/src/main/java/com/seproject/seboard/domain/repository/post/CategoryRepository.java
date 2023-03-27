package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.domain.model.post.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
