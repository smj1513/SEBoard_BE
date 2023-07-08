package com.seproject.board.menu.controller;

import com.seproject.account.account.domain.Account;
import com.seproject.account.role.domain.Role;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.admin.service.MenuExposeService;
import com.seproject.board.menu.application.CategoryAppService;
import com.seproject.board.menu.controller.dto.CategoryResponse;
import com.seproject.board.menu.domain.Menu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "카테고리 API", description = "카테고리(category) 관련 API")
@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class CategoryController {
    private final CategoryAppService categoryAppService;
    private final MenuExposeService menuExposeService;

    @GetMapping
    public ResponseEntity<?> retrieveAllMenu(){

        /**
         * 기존 코드
         * List<CategoryResponse> categoryResponses = categoryAppService.retrieveAllMenu();
         * return new ResponseEntity<>(categoryResponses, HttpStatus.OK);
         */

        Optional<Account> account = SecurityUtils.getAccount();
        List<Role> roles = account.isPresent() ? account.get().getAuthorities() : List.of();

        Map<Menu, List<Menu>> menuHierarchical = categoryAppService.retrieveAllMenu(roles);

        List<CategoryResponse> categoryResponses = new ArrayList<>();

        menuHierarchical.forEach((menu, subMenus) -> {
            CategoryResponse categoryResponse = new CategoryResponse(menu);
            subMenus.stream()
                    .map(CategoryResponse::new)
                    .forEach(categoryResponse::addSubMenu);

            categoryResponses.add(categoryResponse);
        });

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

}
