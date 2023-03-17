package com.seproject.seboard.domain.service;

import com.seproject.seboard.domain.repository.PostRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CategoryService {
    private final PostRepository postRepository;

    public boolean isRemovable(Long categoryId){
        // TODO : postRepo에 CategoryId로 조회해서 하나도 없으면 True, 있으면 false
        return true;
    }
}
