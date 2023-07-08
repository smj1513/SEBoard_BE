package com.seproject.board.post.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.board.post.controller.dto.AdminPostRequest.AdminPostRetrieveCondition;
import com.seproject.board.post.controller.dto.AdminPostResponse.AdminDeletedPostResponse;
import com.seproject.board.post.controller.dto.AdminPostResponse.AdminPostRetrieveResponse;
import com.seproject.board.post.domain.repository.AdminPostSearchRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.board.menu.domain.Category;
import com.seproject.member.domain.BoardUser;
import com.seproject.board.post.domain.model.Post;
import com.seproject.board.menu.domain.repository.CategoryRepository;
import com.seproject.board.common.domain.repository.ReportRepository;
import com.seproject.member.domain.repository.BoardUserRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.seproject.board.common.utils.AppServiceHelper.findByIdOrThrow;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPostAppService {

    private final PostRepository postRepository;
    private final ReportRepository reportRepository;
    private final BoardUserRepository boardUserRepository;
    private final AdminPostSearchRepository adminPostSearchRepository;
    private final CategoryRepository categoryRepository;

    public Page<AdminDeletedPostResponse> findDeletedPostList(Pageable pageable){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        return adminPostSearchRepository.findDeletedPostList(pageable);
    }


    public Page<AdminPostRetrieveResponse> findPostList(AdminPostRetrieveCondition condition, int page, int perPage) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        return adminPostSearchRepository.findPostListByCondition(condition, PageRequest.of(page, perPage));
    }

    public void enrollPin(Long accountId, Long postId) {
        BoardUser requestUser = findByIdOrThrow(accountId, boardUserRepository, "");
        //TODO: 권한처리

        Post post = findByIdOrThrow(postId, postRepository, "");
        post.changePin(true);
    }

    public void cancelPin(Long accountId, Long postId) {
        BoardUser requestUser = findByIdOrThrow(accountId, boardUserRepository, "");
        //TODO: 권한처리

        Post post = findByIdOrThrow(postId, postRepository, "");
        post.changePin(false);
    }

    public void restorePost(Long postId){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        post.restore();
        reportRepository.deleteAllByPostId(post.getPostId());
    }

    public void restoreBulkPost(List<Long> postIds) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        postRepository.findAllById(postIds).forEach(post -> {
            post.restore();
            reportRepository.deleteAllByPostId(post.getPostId());
        });
    }

    public void deleteBulkPost(List<Long> postIds, boolean isPermanent) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        postRepository.findAllById(postIds).forEach(post -> {
            post.delete(isPermanent);
        });
    }

    public void migratePost(Long fromCategoryId, Long toCategoryId){
        Category from = categoryRepository.findById(fromCategoryId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));
        Category to = categoryRepository.findById(toCategoryId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));

        //TODO : bulk update 적용
        postRepository.findByCategoryId(fromCategoryId).forEach(post -> {
            post.changeCategory(to);
        });
    }

}