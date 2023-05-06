package com.seproject.seboard.domain.repository.category;

import com.seproject.seboard.domain.model.category.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
