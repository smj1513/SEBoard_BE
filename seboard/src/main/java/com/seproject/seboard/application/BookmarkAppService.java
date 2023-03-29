package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.post.Bookmark;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookmarkAppService {
//    private final BookmarkRepository bookmarkRepository;
//    private final PostRepository postRepository;
//    private final CommentRepository commentRepository;
//    /**
//     * TODO: 1. 게시물이 존재하지 않는 경우 예외처리
//     *       2. 사용자가 존재하지 않는경우 예외처리
//     */
//
//    public void enrollBookmark(Long postId,Long userId) {
//        boolean existBookmark = bookmarkRepository.existsByPostIdAndUserId(postId,userId);
//
//        if(!existBookmark) {
//            Bookmark bookmark = Bookmark.builder()
//                    .postId(postId)
//                    .userId(userId)
//                    .build();
//
//            bookmarkRepository.save(bookmark);
//        }
//
//        // pin과 동일하게 이미 존재하는 북마크이기 때문에 else 시 예외 생성은 하지 않았음
//
//    }
//
//    /**
//     * TODO: 1. 게시물이 존재하지 않는 경우 예외처리
//     *       2. 사용자가 존재하지 않는경우 예외처리
//     */
//    public void cancelBookmark(Long postId,Long userId) {
//        boolean existBookmark = bookmarkRepository.existsByPostIdAndUserId(postId,userId);
//
//        if(existBookmark) {
//            Bookmark bookmark = bookmarkRepository.findByPostIdAndUserId(postId,userId);
//            bookmarkRepository.delete(bookmark);
//        }
//
//        // 이미 존재하지 않는 북마크이기 때문에 else 시 예외 생성은 하지 않았음
//    }
//
//    //내가 즐겨찾기한 게시물 목록 조회 -> BookmarkAppService에 있어야 할까 PostAppService에 있어야 할까?
//    public List<BookmarkDTO.BookmarkListResponseDTO> retrieveBookmarkPosts(Long userId) {
//        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);
//        if(bookmarks.size() == 0) {
//            throw new IllegalArgumentException(); // TODO : 메세지 추가
//        }
//
//        //TODO : JPQL 최적화,
//        return bookmarks.stream().map(bookmark -> {
//            Long postId = bookmark.getPostId();
//            Post post = findByIdOrThrow(postId,postRepository,"");
//            int commentSize = commentRepository.getCommentSizeByPostId(post.getPostId());
//            return BookmarkDTO.BookmarkListResponseDTO.toDTO(post,commentSize);
//        }).collect(Collectors.toList());
//
//    }
//
//    private <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repo, String errorMsg){
//        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(errorMsg));
//    }

}