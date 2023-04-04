package com.seproject.seboard.domain.service;

import com.seproject.seboard.domain.repository.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
    private final PostRepository postRepository;

    public boolean hasPost(Long categoryId) {
        return postRepository.existsByCategoryId(categoryId);
    }
}
