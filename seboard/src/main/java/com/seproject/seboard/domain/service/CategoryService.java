package com.seproject.seboard.domain.service;

import com.seproject.seboard.domain.repository.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public boolean hasPost(Long categoryId) {
        return postRepository.existsByCategoryId(categoryId);
    }

    public boolean hasSubCategory(Long categoryId){
        return categoryRepository.existsSubCategory(categoryId);
    }
}
