package com.seproject.board.menu.domain.repository;

import com.seproject.board.menu.domain.BoardMenu;
import com.seproject.board.menu.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findBySuperMenu(BoardMenu superMenu);
}
