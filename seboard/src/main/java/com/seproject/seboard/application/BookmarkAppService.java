package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.Bookmark;
import com.seproject.seboard.domain.repository.BookmarkRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookmarkAppService {
    private final BookmarkRepository bookmarkRepository;

    /**
     * TODO: 1. 게시물이 존재하지 않는 경우 예외처리
     *       2. 사용자가 존재하지 않는경우 예외처리
     */

    public void enrollBookmark(Long postId,Long userId) {
        boolean existBookmark = bookmarkRepository.existsByPostIdAndUserId(postId,userId);

        if(!existBookmark) {
            Bookmark bookmark = Bookmark.builder()
                    .postId(postId)
                    .userId(userId)
                    .build();

            bookmarkRepository.save(bookmark);
        }

        // pin과 동일하게 이미 존재하는 북마크이기 때문에 else 시 예외 생성은 하지 않았음

    }

    /**
     * TODO: 1. 게시물이 존재하지 않는 경우 예외처리
     *       2. 사용자가 존재하지 않는경우 예외처리
     */
    public void cancelBookmark(Long postId,Long userId) {
        boolean existBookmark = bookmarkRepository.existsByPostIdAndUserId(postId,userId);

        if(existBookmark) {
            Bookmark bookmark = bookmarkRepository.findByPostIdAndUserId(postId,userId);
            bookmarkRepository.delete(bookmark);
        }

        // 이미 존재하지 않는 북마크이기 때문에 else 시 예외 생성은 하지 않았음
    }

}