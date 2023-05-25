package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.domain.model.post.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    @Query(value = "select count(*) from bookmarks where boomarks.member_id =(select member_id from members where members.account_id=(select account_id from accounts where accounts.login_id=:loginId))", nativeQuery = true)
    Integer countsBookmarkByLoginId(String loginId);
    @Query(value = "select exists(select * from bookmarks where post_id = :postId and member_id = :memberId)", nativeQuery = true)
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    @Query(value="select * from bookmarks where post_id = :postId and member_id = :memberId", nativeQuery = true)
    Optional<Bookmark> findByPostIdAndMemberId(Long postId, Long memberId);
}