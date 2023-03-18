package com.seproject.seboard.controller;

import com.seproject.seboard.application.PostAppService;
import com.seproject.seboard.dto.PostDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.dto.MessageDTO.*;

@RestController
@RequestMapping("/posts/named")
@AllArgsConstructor
public class NamedPostController {
    private final PostAppService postAppService;

    //TODO : 예외 처리 코드는 어디서?
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseMessageDTO createNamedPost(@RequestBody PostDTO.PostRequestDTO dto, Long userId) {
        //TODO : token을 통해서 userId는 어디서 받나?
        postAppService.createNamedPost(
                dto.getTitle(), dto.getContents(), dto.getCategoryId(), userId, dto.isPined()
        );

        //TODO : message property 로 뺄 것
        return new ResponseMessageDTO("게시글 작성이 완료되었습니다.");
    }

    //TODO : 예외 처리 로직 필요
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{postId}")
    public ResponseMessageDTO updateNamedPost(@RequestBody PostDTO.PostRequestDTO dto,
                                              @PathVariable("postId") Long postId, Long userId) {
        //TODO : token을 통해서 userId는 어디서 받나?
        postAppService.updateNamedPost(
                dto.getTitle(), dto.getContents(), dto.getCategoryId(), postId, userId
        );

        //TODO : message property 로 뺄 것
        return new ResponseMessageDTO("게시글 수정이 완료되었습니다.");
    }

    //TODO : 예외 처리 로직 필요
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{postId}")
    public ResponseMessageDTO deleteNamedPost(@PathVariable("postId") Long postId, Long userId) {
        //TODO : token을 통해서 userId는 어디서 받나?
        postAppService.deleteNamedPost(postId, userId);

        //TODO : message property 로 뺄 것
        return new ResponseMessageDTO("게시글 삭제에 성공하였습니다");
    }
}
