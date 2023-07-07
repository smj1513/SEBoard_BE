package com.seproject.board.menu.service;

import com.seproject.board.menu.domain.repository.MenuRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
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
