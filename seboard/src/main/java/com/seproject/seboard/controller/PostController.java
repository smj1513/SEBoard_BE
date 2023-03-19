package com.seproject.seboard.controller;

import com.seproject.seboard.application.PostAppService;
import com.seproject.seboard.dto.PostDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostAppService postAppService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostDTO.PostListResponseDTO> retrievePostList(@RequestParam Long categoryId,
                                                              @RequestParam(required = false) int page,
                                                              @RequestParam(required = false) int perPage){
        return postAppService.retrievePostList(categoryId);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO.PostResponseDTO retrievePost(@PathVariable("postId") Long postId, Long userId){
        return postAppService.retrieveNamedPost(postId, userId);
    }
}
