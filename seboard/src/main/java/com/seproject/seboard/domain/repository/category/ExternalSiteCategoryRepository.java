package com.seproject.seboard.domain.repository.category;

import com.seproject.seboard.domain.model.category.ExternalSiteCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalSiteCategoryRepository extends JpaRepository<ExternalSiteCategory, Long> {
}
