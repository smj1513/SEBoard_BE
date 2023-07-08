package com.seproject.board.bulletin.controller;


import com.seproject.board.bulletin.domain.model.MainPageMenu;
import com.seproject.board.bulletin.application.MainPageService;
import com.seproject.board.post.application.PostSearchAppService;
import com.seproject.board.menu.domain.Menu;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.seproject.board.post.controller.dto.PostResponse.*;
import static com.seproject.board.bulletin.controller.dto.MainPageDTO.*;

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
