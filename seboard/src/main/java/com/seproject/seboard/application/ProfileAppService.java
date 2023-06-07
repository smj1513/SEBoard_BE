package com.seproject.seboard.application;

import com.seproject.account.model.account.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.controller.dto.comment.CommentResponse.RetrieveCommentProfileElement;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponseElement;
import com.seproject.seboard.controller.dto.profile.ProfileResponse.ProfileInfoResponse;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileAppService {
    private final PostSearchRepository postSearchRepository;
    private final CommentRepository commentRepository;
    private final CommentSearchRepository commentSearchRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    public ProfileInfoResponse retrieveProfileInfo(String loginId){
        Account account = SecurityUtils.getAccount().orElse(null);
        //TODO : SQL 쿼리 변경 필요
        Integer postCount = null;
        Integer commentCount = null;
        Integer bookmarkCount = null;
        String nickname = memberRepository.findByLoginId(loginId).orElseThrow(()-> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER)).getName();

        if(account != null && account.getLoginId().equals(loginId)){
            postCount = postSearchRepository.countsPostByLoginId(loginId);
            commentCount = commentSearchRepository.countsCommentByLoginId(loginId);
            bookmarkCount = bookmarkRepository.countsBookmarkByLoginId(loginId);
        }else{
            postCount = postSearchRepository.countsMemberPostByLoginId(loginId);
            commentCount = commentSearchRepository.countsMemberCommentByLoginId(loginId);
        }

        return ProfileInfoResponse.builder()
                .nickname(nickname)
                .postCount(postCount)
                .commentCount(commentCount)
                .bookmarkCount(bookmarkCount)
                .build();
    }

    public Page<RetrievePostListResponseElement> retrieveMyPost(String loginId, int page, int perPage){
        Account account = SecurityUtils.getAccount().orElse(null);
        Page<RetrievePostListResponseElement> posts;

        if(account == null || !account.getLoginId().equals(loginId)){
            posts = postSearchRepository.findMemberPostByLoginId(loginId, PageRequest.of(page, perPage));

        }else{
            posts = postSearchRepository.findPostByLoginId(loginId, PageRequest.of(page, perPage));
        }

        posts.getContent().forEach(post -> {
            int commentSize = commentRepository.countCommentsByPostId(post.getPostId());
            post.setCommentSize(commentSize);
        });

        return posts;
    }

    public Page<RetrievePostListResponseElement> retrieveBookmarkPost(String loginId, int page, int perPage){
        Account account = SecurityUtils.getAccount().orElse(null);

        if(account == null){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        Page<RetrievePostListResponseElement> posts = postSearchRepository.findBookmarkPostByLoginId(loginId, PageRequest.of(page, perPage));
        posts.getContent().forEach(post -> {
            int commentSize = commentRepository.countCommentsByPostId(post.getPostId());
            post.setCommentSize(commentSize);
        });

        return posts;
    }

    public Page<RetrieveCommentProfileElement> retrieveMyComment(String loginId, int page, int perPage){
        Account account = SecurityUtils.getAccount().orElse(null);

        if(account == null || !account.getLoginId().equals(loginId)){
            return commentSearchRepository.findMemberCommentByLoginId(loginId, PageRequest.of(page, perPage));
        }else{
            return commentSearchRepository.findCommentByLoginId(loginId, PageRequest.of(page, perPage));
        }
    }

}
