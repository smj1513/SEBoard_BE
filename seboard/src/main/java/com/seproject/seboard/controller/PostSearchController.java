package com.seproject.seboard.controller;

import com.seproject.seboard.application.PostSearchAppService;
import com.seproject.seboard.controller.dto.post.PostResponse;
import com.seproject.seboard.controller.dto.search.PostSearchRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search/posts")
@AllArgsConstructor
public class PostSearchController {
    private final PostSearchAppService postSearchAppService;

//    @GetMapping
//    public ResponseEntity searchPosts(@RequestBody PostSearchRequest request,
//                                      @RequestParam int page, @RequestParam int perPage) {
//        switch(PostSearchOptions.valueOf(request.getSearchOption())) {
//            case TITLE:{
//                PostResponse.RetrievePostListResponse data =
//                        postSearchAppService.searchByTitle(request.getQuery(), page, perPage);
//                return ResponseEntity.ok().body(data);
//            }
//            case CONTENT:{
//                PostResponse.RetrievePostListResponse data =
//                        postSearchAppService.searchByContent(request.getQuery(), page, perPage);
//                return ResponseEntity.ok().body(data);
//            }
//            case TITLE_OR_CONTENT:{
//                PostResponse.RetrievePostListResponse data =
//                        postSearchAppService.searchByTitleOrContent(request.getQuery(), page, perPage);
//                return ResponseEntity.ok().body(data);
//            }
//            case AUTHOR:{
//                PostResponse.RetrievePostListResponse data =
//                        postSearchAppService.searchByAuthorName(request.getQuery(), page, perPage);
//                return ResponseEntity.ok().body(data);
//            }
//            case ALL:{
//                PostResponse.RetrievePostListResponse data =
//                        postSearchAppService.searchByAll(request.getQuery(), page, perPage);
//                return ResponseEntity.ok().body(data);
//            }
//            default:
//                return ResponseEntity.badRequest().build();
//        }
//    }
}
