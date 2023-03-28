package com.seproject.seboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.dto.post.CategoryRequest.*;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest request) {

        /**
         * TODO : jwt
         *         존재하지 않는 상위 카테고리
         *         이미 존재하는 카테고리 이름
         *         권한 없음
         */

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> retrieveCategoryList(@PathVariable Long categoryId) {

        /**
         * TODO :
         *    존재하지 않는 대분류 카테고리
         */

        return new ResponseEntity<>(categoryId, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategoryList(@PathVariable Long categoryId, @RequestBody UpdateCategoryRequest request) {

        /**
         * TODO : jwt
         *    권한 없음
         *    존재하지 않는 상위 카테고리
         *    이미 존재하는 카테고리 이름
         */

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategoryList(@PathVariable Long categoryId) {

        /**
         * TODO : jwt
         *    권한 없음
         *    대분류 삭제시 하위 카테고리 없어야함
         */

        return new ResponseEntity<>(categoryId, HttpStatus.OK);
    }

}
