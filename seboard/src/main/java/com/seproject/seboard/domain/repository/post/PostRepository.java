package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.domain.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
