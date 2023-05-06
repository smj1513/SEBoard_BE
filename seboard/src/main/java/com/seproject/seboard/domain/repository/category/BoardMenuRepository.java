package com.seproject.seboard.domain.repository.category;

import com.seproject.seboard.domain.model.category.BoardMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMenuRepository extends JpaRepository<BoardMenu, Long> {
}
