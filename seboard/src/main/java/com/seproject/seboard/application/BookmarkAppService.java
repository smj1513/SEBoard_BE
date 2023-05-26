package com.seproject.seboard.application;

import com.seproject.account.model.Account;
import com.seproject.account.repository.AccountRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomUserNotFoundException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.domain.model.post.Bookmark;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.seproject.seboard.application.utils.AppServiceHelper.findByIdOrThrow;

@Service
@AllArgsConstructor
@Transactional
public class BookmarkAppService {
    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    public void enrollBookmark(Long postId, String loginId) {
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        //TODO : member 없을 때 처리 필요, member 생성 or error?
        Member member = memberRepository.findByAccountId(account.getAccountId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        boolean existBookmark = bookmarkRepository.existsByPostIdAndMemberId(postId, member.getBoardUserId());

        if (!existBookmark) {
            Bookmark bookmark = Bookmark.builder()
                    .markedPost(post)
                    .member(member)
                    .build();

            bookmarkRepository.save(bookmark);
        }
    }

    public void cancelBookmark(Long postId, String loginId) {
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));
        //TODO : member 없을 때 처리 필요, member 생성 or error?
        Member member = memberRepository.findByAccountId(account.getAccountId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER));

        Optional<Bookmark> bookmarkBox = bookmarkRepository.findByPostIdAndMemberId(postId, member.getBoardUserId());

        if(bookmarkBox.isPresent()){
            bookmarkRepository.delete(bookmarkBox.get());
        }
    }

    //내가 즐겨찾기한 게시물 목록 조회 -> BookmarkAppService에 있어야 할까 PostAppService에 있어야 할까?
//    public List<BookmarkDTO.BookmarkListResponseDTO> retrieveBookmarkPosts(Long userId) {
//        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);
//        if (bookmarks.size() == 0) {
//            throw new IllegalArgumentException(); // TODO : 메세지 추가
//        }
//
//        //TODO : JPQL 최적화,
//        return bookmarks.stream().map(bookmark -> {
//            Long postId = bookmark.getPostId();
//            Post post = findByIdOrThrow(postId, postRepository, "");
//            int commentSize = commentRepository.getCommentSizeByPostId(post.getPostId());
//            return BookmarkDTO.BookmarkListResponseDTO.toDTO(post, commentSize);
//        }).collect(Collectors.toList());
//
//    }
}