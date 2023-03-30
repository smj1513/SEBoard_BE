package com.seproject.seboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.dto.post.CategoryRequest.*;

@Tag(name = "카테고리 API", description = "카테고리(category) 관련 API")
@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {

    @Parameter(name = "request", description = "상위 카테고리, 생성할 카테고리 이름 정보")
    @Operation(summary = "하위 카테고리 생성", description = "소분류 카테고리를 생성한다")
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

    @Parameter(name = "categoryId", description = "대분류 카테고리 pk")
    @Operation(summary = "하위 카테고리 조회", description = "대분류 카테고리 하위에 있는 모든 소분류 카테고리를 조회한다")
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> retrieveCategoryList(@PathVariable Long categoryId) {

        /**
         * TODO :
         *    존재하지 않는 대분류 카테고리
         */

        return new ResponseEntity<>(categoryId, HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "categoryId", description = "수정할 카테고리 pk"),
                    @Parameter(name = "request", description = "수정할 대분류 카테고리 pk, 카테고리 이름 정보")
            }
    )
    @Operation(summary = "하위 카테고리 수정", description = "소분류 카테고리를 수정한다")
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody UpdateCategoryRequest request) {

        /**
         * TODO : jwt
         *    권한 없음
         *    존재하지 않는 상위 카테고리
         *    이미 존재하는 카테고리 이름
         */

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @Parameter(name = "categoryId", description = "삭제할 카테고리 pk")
    @Operation(summary = "하위 카테고리 삭제", description = "소분류 카테고리를 삭제한다.")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {

        /**
         * TODO : jwt
         *    권한 없음
         *    대분류 삭제시 하위 카테고리 없어야함
         */

        return new ResponseEntity<>(categoryId, HttpStatus.OK);
    }

}
