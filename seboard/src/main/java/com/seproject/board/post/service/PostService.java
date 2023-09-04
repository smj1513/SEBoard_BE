package com.seproject.board.post.service;

import com.seproject.board.post.domain.model.Post;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.NoSuchResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(()->new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));
    }

    public List<Post> findAllByIds(List<Long> ids) {
        List<Post> allById = postRepository.findAllById(ids);
        return allById;
    }
}
