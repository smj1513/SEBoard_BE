package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.user.BoardUser;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.repository.user.BoardUserRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.seproject.seboard.application.utils.AppServiceHelper.findByIdOrThrow;

@Service
@RequiredArgsConstructor
public class PostManageAppService {

    private final PostRepository postRepository;
    private final BoardUserRepository boardUserRepository;

    public void enrollPin(Long accountId, Long postId) {
        BoardUser requestUser = findByIdOrThrow(accountId, boardUserRepository, "");
        //TODO: 권한처리

        Post post = findByIdOrThrow(postId, postRepository, "");
        post.changePin(true);
    }

    public void cancelPin(Long accountId, Long postId) {
        BoardUser requestUser = findByIdOrThrow(accountId, boardUserRepository, "");
        //TODO: 권한처리

        Post post = findByIdOrThrow(postId, postRepository, "");
        post.changePin(false);
    }
}
