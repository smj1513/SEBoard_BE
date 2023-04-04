package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.domain.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    boolean existsByCategoryId(Long categoryId);

    List<Post> findByCategoryId(Long categoryId);
}
