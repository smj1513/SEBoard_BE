package com.seproject.board.post.domain.repository;

import com.seproject.board.post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Query(value = "select exists(select * from posts where category_id = :categoryId)", nativeQuery = true)
    boolean existsByCategoryId(@Param("categoryId") Long categoryId);

    @Query(value = "select * from posts where category_id = :categoryId", nativeQuery = true)
    List<Post> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query(value = "select p from Post p join p.author join p.author.account where p.author.account.accountId = :accountId")
    List<Post> findByAccountId(@Param("accountId") Long accountId);
}
