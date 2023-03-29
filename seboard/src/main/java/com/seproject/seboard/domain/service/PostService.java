package com.seproject.seboard.domain.service;

import com.seproject.seboard.domain.model.post.Category;
import com.seproject.seboard.domain.repository.post.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    //TODO: 프로퍼티로 빼야함
    private static final Integer TITLE_MAX_SIZE = 50;
    private static final Integer TITLE_MIN_SIZE = 4;
    private static final Integer CONTENTS_MAX_SIZE = 1000;
    private static final Integer CONTENTS_MIN_SIZE = 10;

    private final CategoryRepository categoryRepository;


    // 카테고리 아이디 유무
    public boolean existCategory(Long categoryId){
        Optional<Category> category = categoryRepository.findById(categoryId);

        if(category.isEmpty()){
            return false;
        }

        return true;
    }

    // 제목 최대 자리수
    public boolean isValidTitle(String title) {
        return TITLE_MIN_SIZE < title.length() && title.length() <= TITLE_MAX_SIZE ;
    }

    // 본문 최대 자리수
    public boolean isValidContents(String contents) {
        return CONTENTS_MIN_SIZE < contents.length() && contents.length() <= CONTENTS_MAX_SIZE ;
    }
}
