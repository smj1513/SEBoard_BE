package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.domain.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Query(value = "select exists(select * from posts where category_id = :categoryId)", nativeQuery = true)
    boolean existsByCategoryId(Long categoryId);

    @Query(value = "select * from posts where category_id = :categoryId", nativeQuery = true)
    List<Post> findByCategoryId(Long categoryId);

    @Query(value = "select p from Post p join p.author join p.author.account where p.author.account.accountId = :accountId")
    List<Post> findByAccountId(Long accountId);
}
