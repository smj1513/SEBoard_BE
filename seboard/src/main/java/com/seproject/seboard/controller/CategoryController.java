package com.seproject.seboard.controller;

import com.seproject.seboard.application.CategoryAppService;
import com.seproject.seboard.controller.dto.post.CategoryRequest;
import com.seproject.seboard.controller.dto.post.CategoryRequest.CreateCategoryRequest;
import com.seproject.seboard.controller.dto.post.CategoryRequest.UpdateCategoryRequest;
import com.seproject.seboard.controller.dto.post.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "카테고리 API", description = "카테고리(category) 관련 API")
@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class CategoryController {
    private final CategoryAppService categoryAppService;

    @Parameter(name = "request", description = "상위 카테고리, 생성할 카테고리 이름 정보")
    @Operation(summary = "하위 카테고리 생성", description = "카테고리를 생성한다")
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest request, @RequestParam String categoryType) {

        /**
         * TODO : jwt
         *         존재하지 않는 상위 카테고리
         *         이미 존재하는 카테고리 이름
         *         권한 없음
         */
        categoryAppService.createCategory(request.toCommand(categoryType));

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> retrieveAllMenu(){
        List<CategoryResponse> categoryResponses = categoryAppService.retrieveAllMenu();
        return new ResponseEntity<>(categoryResponses, HttpStatus.OK);
    }

    @Parameter(name = "categoryId", description = "대분류 카테고리 pk")
    @Operation(summary = "하위 카테고리 조회", description = "대분류 카테고리 하위에 있는 모든 소분류 카테고리를 조회한다")
    @GetMapping("/{menuId}")
    public ResponseEntity<?> retrieveMenuById(@PathVariable Long menuId) {

        /**
         * TODO :
         *    존재하지 않는 대분류 카테고리
         */
        CategoryResponse categoryResponse = categoryAppService.retrieveMenuById(menuId);

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
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
        categoryAppService.updateCategory(request.toCommand(categoryId));

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
        categoryAppService.deleteCategory(categoryId);

        return new ResponseEntity<>(categoryId, HttpStatus.OK);
    }

    @PostMapping("/migrate")
    public ResponseEntity<?> migrateCategory(@RequestParam Long from,@RequestParam Long to){
        categoryAppService.migrateCategory(from,to);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
