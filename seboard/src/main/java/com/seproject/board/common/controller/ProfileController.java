package com.seproject.board.common.controller;

import com.seproject.board.common.application.ProfileAppService;
import com.seproject.board.comment.controller.dto.CommentResponse.RetrieveCommentProfileElement;
import com.seproject.board.post.controller.dto.PostResponse.RetrievePostListResponseElement;
import com.seproject.board.common.controller.dto.ProfileResponse.ProfileInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileAppService profileAppService;
    @GetMapping("/{loginId}")
    public ResponseEntity<ProfileInfoResponse> retrieveProfileInfo(@PathVariable String loginId){
        ProfileInfoResponse profileInfoResponse = profileAppService.retrieveProfileInfo(loginId);
        return ResponseEntity.ok().body(profileInfoResponse);
    }

    @GetMapping("/{loginId}/posts")
    public ResponseEntity<Page<RetrievePostListResponseElement>> retrieveMyPosts(@PathVariable String loginId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int perPage){
        Page<RetrievePostListResponseElement> res = profileAppService.retrieveMyPost(loginId, page, perPage);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{loginId}/bookmarks")
    public ResponseEntity<Page<RetrievePostListResponseElement>> retrieveBookmarkPost(@PathVariable String loginId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int perPage){
        Page<RetrievePostListResponseElement> res = profileAppService.retrieveBookmarkPost(loginId, page, perPage);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{loginId}/comments")
    public ResponseEntity<Page<RetrieveCommentProfileElement>> retrieveMyComments(
            @PathVariable String loginId, @RequestParam(defaultValue ="0") int page,
            @RequestParam(defaultValue = "10") int perPage){
        Page<RetrieveCommentProfileElement> res = profileAppService.retrieveMyComment(loginId, page, perPage);
        return ResponseEntity.ok().body(res);
    }
}