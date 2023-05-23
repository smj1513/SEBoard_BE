package com.seproject.admin.domain.repository;

import com.seproject.seboard.domain.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface AdminPostSearchRepository extends Repository<Post, Long> {
//    Page<Post> findPostListByFilter(Long postId, Pageable pageable);
}
