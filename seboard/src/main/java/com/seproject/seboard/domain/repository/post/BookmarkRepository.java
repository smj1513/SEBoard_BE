package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.domain.model.post.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    Optional<Bookmark> findByPostIdAndMemberId(Long postId, Long boardUserId);
}