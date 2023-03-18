package com.seproject.seboard.controller;

import com.seproject.seboard.application.PostAppService;
import com.seproject.seboard.dto.AuthorDTO;
import com.seproject.seboard.dto.PostDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.dto.MessageDTO.*;

@RestController
@RequestMapping("/posts/unnamed")
@AllArgsConstructor
public class UnnamedPostController {
    private final PostAppService postAppService;

    //TODO : 예외 처리 코드는 어디서?
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseMessageDTO createUnnamedPost(@RequestBody PostDTO.UnnamedPostCreationRequestDTO dto) {
        //TODO : token을 통해서 userId는 어디서 받나?
        postAppService.createUnnamedPost(
                dto.getTitle(), dto.getContents(), dto.getCategoryId(),
                dto.getAuthor().getName(), dto.getAuthor().getPassword()
        );


        //TODO : message property 로 뺄 것
        return new ResponseMessageDTO("게시글이 성공적으로 등록되었습니다.");
    }

    //TODO : 예외 처리 로직 필요
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{postId}")
    public ResponseMessageDTO updateUnnamedPost(@RequestBody PostDTO.UnnamedPostUpdatingRequestDTO dto,
                                              @PathVariable("postId") Long postId) {
        //TODO : token을 통해서 userId는 어디서 받나?
        postAppService.updateUnnamedPost(
                dto.getTitle(), dto.getContents(), dto.getCategoryId(), postId, dto.getAuthor().getPassword()
        );

        //TODO : message property 로 뺄 것
        return new ResponseMessageDTO("익명 게시글이 수정되었습니다.");
    }

    //TODO : 예외 처리 로직 필요
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{postId}")
    public ResponseMessageDTO deleteUnnamedPost(@RequestBody AuthorDTO.AnonymousVerifyingDTO dto,
                                                @PathVariable("postId") Long postId) {
        //TODO : token을 통해서 userId는 어디서 받나?
        postAppService.deleteUnnamedPost(postId, dto.getPassword());

        //TODO : message property 로 뺄 것
        return new ResponseMessageDTO("게시글 삭제에 성공하였습니다");
    }
}
