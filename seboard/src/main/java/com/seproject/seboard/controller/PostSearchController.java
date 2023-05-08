package com.seproject.seboard.controller;

import com.seproject.seboard.application.PostSearchAppService;
import com.seproject.seboard.controller.dto.post.PostResponse;
import com.seproject.seboard.controller.dto.search.PostSearchRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.controller.dto.post.PostResponse.*;

@RestController
@RequestMapping("/search/posts")
@AllArgsConstructor
public class PostSearchController {
    private final PostSearchAppService postSearchAppService;

    @GetMapping
    public ResponseEntity searchPosts(@ModelAttribute PostSearchRequest request,
                                      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int perPage) {
        switch(PostSearchOptions.valueOf(request.getSearchOption())) {
            case TITLE:{
                Page<RetrievePostListResponseElement> res = postSearchAppService.searchByTitle(request.getQuery(), page, perPage);
                return ResponseEntity.ok().body(res);
            }
            case CONTENT:{
                Page<RetrievePostListResponseElement> res = postSearchAppService.searchByContent(request.getQuery(), page, perPage);
                return ResponseEntity.ok().body(res);
            }
            case TITLE_OR_CONTENT:{
                Page<RetrievePostListResponseElement> res = postSearchAppService.searchByTitleOrContent(request.getQuery(), page, perPage);
                return ResponseEntity.ok().body(res);
            }
            case AUTHOR:{
                Page<RetrievePostListResponseElement> res = postSearchAppService.searchByAuthorName(request.getQuery(), page, perPage);
                return ResponseEntity.ok().body(res);
            }
            case ALL:{
                Page<RetrievePostListResponseElement> res = postSearchAppService.searchByAll(request.getQuery(), page, perPage);
                return ResponseEntity.ok().body(res);
            }
            default:
                return ResponseEntity.badRequest().build();
        }
    }
}
