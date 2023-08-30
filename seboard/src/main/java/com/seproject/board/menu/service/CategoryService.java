package com.seproject.board.menu.service;

import com.seproject.board.menu.domain.BoardMenu;
import com.seproject.board.menu.domain.Category;
import com.seproject.board.menu.domain.Menu;
import com.seproject.board.menu.domain.repository.MenuRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryService {
    private final PostRepository postRepository;
    private final MenuRepository menuRepository;

    public boolean hasPost(Long categoryId) {
        return postRepository.existsByCategoryId(categoryId);
    }

    public boolean hasSubCategory(Long categoryId){
        return menuRepository.existsSubMenuById(categoryId);
    }
}
