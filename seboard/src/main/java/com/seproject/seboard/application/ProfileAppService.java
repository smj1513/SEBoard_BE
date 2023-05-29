package com.seproject.seboard.application;

import com.seproject.account.model.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.controller.dto.post.PostResponse;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponseElement;
import com.seproject.seboard.controller.dto.profile.ProfileResponse;
import com.seproject.seboard.controller.dto.profile.ProfileResponse.ProfileInfoResponse;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileAppService {
    private final PostSearchRepository postSearchRepository;
    private final CommentSearchRepository commentSearchRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    public ProfileInfoResponse retrieveProfileInfo(String loginId){
        Account account = SecurityUtils.getAccount().orElse(null);
        //TODO : SQL 쿼리 변경 필요
        Integer postCount = postSearchRepository.countsPostByLoginId(loginId);
        Integer commentCount = commentSearchRepository.countsCommentByLoginId(loginId);
        Integer bookmarkCount = null;
        String nickname = memberRepository.findByLoginId(loginId).orElseThrow(()-> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER)).getName();

        if(account != null && account.getLoginId().equals(loginId)){
            bookmarkCount = bookmarkRepository.countsBookmarkByLoginId(loginId);
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

        if(account == null || !account.getLoginId().equals(loginId)){
            //TODO : 쿼리로 변경필요
            return postSearchRepository.findMemberPostByLoginId(loginId, PageRequest.of(page, perPage));
        }else{
            return postSearchRepository.findPostByLoginId(loginId, PageRequest.of(page, perPage));
        }
    }

    public Page<RetrievePostListResponseElement> retrieveBookmarkPost(String loginId, int page, int perPage){
        Account account = SecurityUtils.getAccount().orElse(null);

        if(account == null){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        return postSearchRepository.findBookmarkPostByLoginId(loginId, PageRequest.of(page, perPage));
    }

}
