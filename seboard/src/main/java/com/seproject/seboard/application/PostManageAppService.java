package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.user.User;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.repository.user.BoardUserRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostManageAppService {

    private final PostRepository postRepository;
    private final BoardUserRepository boardUserRepository;

    public void enrollPin(Long userId, Long postId) {

        User requestUser = findByIdOrThrow(userId, boardUserRepository, "");
        Post post = findByIdOrThrow(postId, postRepository, "");

        //TODO : 인가 처리
        post.pin();
    }

    public void cancelPin(Long userId, Long postId) {

        User requestUser = findByIdOrThrow(userId, boardUserRepository, "");
        Post post = findByIdOrThrow(postId, postRepository, "");

        //TODO : 인가 처리
        post.unPin();
    }

    private <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repo, String errorMsg){
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(errorMsg));
    }
}
