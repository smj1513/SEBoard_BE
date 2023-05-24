package com.seproject.seboard.controller;


import com.seproject.admin.domain.MainPageMenu;
import com.seproject.admin.service.MainPageService;
import com.seproject.seboard.application.PostSearchAppService;
import com.seproject.seboard.domain.model.category.Menu;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.seproject.seboard.controller.dto.post.PostResponse.*;
import static com.seproject.seboard.controller.dto.MainPageDTO.*;

@Tag(name = "메인 페이지 API", description = "메인 페이지(mainPage) 관련 API")
@RestController
@RequestMapping("/mainPage")
@AllArgsConstructor
public class MainPageController {

    private final PostSearchAppService postSearchAppService;
    private final MainPageService mainPageService;

    @GetMapping
    public ResponseEntity<?> retrieveMainPagePost(){
        List<MainPageMenu> mainPageMenus = mainPageService.retrieveAllMainPageMenus();
        List<RetrieveMainPageResponse> responses = new ArrayList<>();
        for (MainPageMenu mainPageMenu : mainPageMenus) {
            Menu menu = mainPageMenu.getMenu();
            Page<RetrievePostListResponseElement> postList = postSearchAppService.findPostList(menu.getMenuId(), 0, 5);
            responses.add(RetrieveMainPageResponse.toDTO(postList,menu));
        }

        return ResponseEntity.ok(responses);
    }
}
