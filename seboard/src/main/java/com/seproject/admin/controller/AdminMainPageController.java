package com.seproject.admin.controller;

import com.seproject.admin.domain.MainPageMenu;
import com.seproject.admin.service.MainPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.seproject.admin.dto.MainPageDTO.*;

@Tag(name = "게시글 관리 API", description = "관리자 시스템의 게시글 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin")
@Controller
public class AdminMainPageController {

    private final MainPageService mainPageService;

    @Operation(summary = "메인 페이지에 보여줄 메뉴 조회", description = "메인 페이지에 보여줄 메뉴의 종류를 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAllMainPageMenuRequest.class)), responseCode = "200", description = "목록 조회 성공"),
    })
    @GetMapping("/mainPageMenus")
    public ResponseEntity<?> retrieveAllMainPageMenus() {
        List<MainPageMenu> mainPageMenus = mainPageService.retrieveAllMainPageMenus();
        return new ResponseEntity<>(RetrieveAllMainPageMenuRequest.toDTO(mainPageMenus), HttpStatus.OK);
    }

    @Operation(summary = "메인 페이지에 보여줄 메뉴 추가", description = "메인 페이지에 보여줄 메뉴를 추가한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = CreateMainPageMenuResponse.class)), responseCode = "200", description = "메뉴 추가 성공"),
    })
    @PostMapping("/mainPageMenus")
    public ResponseEntity<?> addMainPageMenus(@RequestBody CreateMainPageMenuRequest request) {
        //TODO : Menu만?
        MainPageMenu mainPageMenu = mainPageService.createMainPageMenu(request.getMenuId());
        return new ResponseEntity<>(CreateMainPageMenuResponse.toDTO(mainPageMenu),HttpStatus.OK);
    }

    @Operation(summary = "메인 페이지에 보여줄 메뉴 삭제", description = "메인 페이지에 보여줄 메뉴를 삭제한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = DeleteMainPageMenuResponse.class)), responseCode = "200", description = "메뉴 추가 실패"),
    })
    @DeleteMapping("/mainPageMenus")
    public ResponseEntity<?> deleteMainPageMenus(@RequestBody DeleteMainPageMenuRequest request) {

        MainPageMenu mainPageMenu = mainPageService.deleteMainPageMenu(request.getId());
        return new ResponseEntity<>(DeleteMainPageMenuResponse.toDTO(mainPageMenu),HttpStatus.OK);
    }
}
