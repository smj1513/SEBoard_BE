package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.Author;
import com.seproject.seboard.domain.model.Post;
import com.seproject.seboard.domain.repository.AuthorRepository;
import com.seproject.seboard.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class PostManageAppService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    public void enrollPin(Long userId, Long postId) {

        Author requestUser = findByIdOrThrow(userId, authorRepository, "");
        Post post = findByIdOrThrow(postId, postRepository, "");

        //TODO : 인가 처리
        post.pin();
    }

    public void cancelPin(Long userId, Long postId) {

        Author requestUser = findByIdOrThrow(userId, authorRepository, "");
        Post post = findByIdOrThrow(postId, postRepository, "");

        //TODO : 인가 처리
        post.unPin();
    }

    private <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repo, String errorMsg){
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(errorMsg));
    }
}
