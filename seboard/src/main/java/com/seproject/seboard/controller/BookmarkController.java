package com.seproject.seboard.controller;

import com.seproject.seboard.application.BookmarkAppService;
import com.seproject.seboard.dto.MessageDTO;
import com.seproject.seboard.dto.MessageDTO.ResponseMessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/bookmark")
@AllArgsConstructor
public class BookmarkController {
    private final BookmarkAppService bookmarkAppService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseMessageDTO enrollBookmark(@PathVariable Long postId, Long userId){
        bookmarkAppService.enrollBookmark(postId, userId);
        return new ResponseMessageDTO("북마크가 등록되었습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public ResponseMessageDTO cancelBookmark(@PathVariable Long postId, Long userId){
        bookmarkAppService.cancelBookmark(postId, userId);
        return new ResponseMessageDTO(""); //TODO : message
    }
}
