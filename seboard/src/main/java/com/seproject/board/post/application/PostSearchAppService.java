package com.seproject.board.post.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.role.domain.Role;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.board.menu.domain.Category;
import com.seproject.board.post.persistence.PostQueryRepository;
import com.seproject.board.post.service.BookmarkService;
import com.seproject.board.post.service.PostService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.*;
import com.seproject.board.post.controller.dto.PostResponse.RetrievePostDetailResponse;
import com.seproject.board.post.controller.dto.PostResponse.RetrievePostListResponseElement;
import com.seproject.board.menu.domain.BoardMenu;
import com.seproject.board.post.domain.model.Post;
import com.seproject.board.post.domain.model.exposeOptions.ExposeState;
import com.seproject.member.domain.Member;
import com.seproject.board.menu.domain.repository.BoardMenuRepository;
import com.seproject.board.comment.domain.repository.CommentRepository;
import com.seproject.board.post.domain.repository.PostSearchRepository;
import com.seproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostSearchAppService {
    private final PostSearchRepository postSearchRepository;
    private final CommentRepository commentRepository;
    private final BoardMenuRepository boardMenuRepository;


    private final PostQueryRepository postQueryRepository;
    private final MemberService memberService;
    private final PostService postService;
    private final BookmarkService bookmarkService;


    @Transactional // TODO : 조회수 늘리기 다른 방법으로 변경
    public RetrievePostDetailResponse findPostDetail(Long postId, String password){

        Account account = SecurityUtils.getAccount().orElse(null);
        Post post = postQueryRepository.findByIdWithAll(postId);
        Category category = post.getCategory();
        RetrievePostDetailResponse postDetailResponse = new RetrievePostDetailResponse(post);

        boolean isEditable = false;
        boolean isBookmarked = false;
        boolean isAuthor = false;

        if(account != null) {
            Member member = memberService.findByAccountId(account.getAccountId());
            isBookmarked = bookmarkService.isBookmarked(post, member);
            isAuthor = post.isWrittenBy(account.getAccountId());

            isEditable = category.editable(account.getRoles()) || isAuthor;
        }

        postDetailResponse.setEditable(isEditable);
        postDetailResponse.setBookmarked(isBookmarked);

        ExposeState exposeState = post.getExposeOption().getExposeState();
        if(exposeState == ExposeState.PRIVACY) {

            if (account == null) {
                throw new CustomAuthenticationException(ErrorCode.NOT_LOGIN,null);
            }

            if (isAuthor || category.manageable(account.getRoles())) {
                post.increaseViews();
                return postDetailResponse;
            }

            if(!post.checkPassword(password)) {
                throw new CustomIllegalArgumentException(ErrorCode.INCORRECT_POST_PASSWORD,null);
            }

        } else if(exposeState == ExposeState.KUMOH) {

            if (account == null) {
                throw new CustomAuthenticationException(ErrorCode.NOT_LOGIN,null);
            }

            if (isAuthor || category.manageable(account.getRoles())) {
                post.increaseViews();
                return postDetailResponse;
            }

            boolean isKumoh = account.getRoles().stream()
                    .anyMatch(role -> role.getAuthority().equals(Role.ROLE_KUMOH));

            if (!isKumoh) {
                throw new CustomAccessDeniedException(ErrorCode.ACCESS_DENIED,null);
            }
        }

        post.increaseViews();
        return postDetailResponse;
    }

    @Transactional
    public RetrievePostDetailResponse findPostDetail(Long postId){
        return findPostDetail(postId, "");
    }

    public List<RetrievePostListResponseElement> findPinedPostList(Long categoryId) {
        BoardMenu boardMenu = boardMenuRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));

        Optional<Account> optional = SecurityUtils.getAccount();
        List<Role> roles = optional.isPresent() ? optional.get().getRoles() : Collections.emptyList();

        List<RetrievePostListResponseElement> postPage;

        if(boardMenu.exposable(roles)) {
            postPage = postSearchRepository.findPinedPostByCategoryId(categoryId);

            postPage.forEach(postDto -> { // TODO : N+1
                int commentSize = commentRepository.countCommentsByPostId(postDto.getPostId());
                postDto.setCommentSize(commentSize);
            });

            return postPage;
        }

        throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
    }

    public Page<RetrievePostListResponseElement> findPostList(Long categoryId, int page, int size){
        BoardMenu boardMenu = boardMenuRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));

        Optional<Account> optional = SecurityUtils.getAccount();
        List<Role> roles = optional.isPresent() ? optional.get().getRoles() : Collections.emptyList();

        if(boardMenu.editable(roles)) {
            Page<RetrievePostListResponseElement> postPage =
                    postSearchRepository.findPostByCategoryId(categoryId, PageRequest.of(page, size));
            setCommentSize(postPage);

            return postPage;
        }

        throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
    }

    public Page<RetrievePostListResponseElement> searchByTitle(String title, int page, int perPage){
        Page<RetrievePostListResponseElement> postPage =
                postSearchRepository.findByTitle(title, PageRequest.of(page, perPage));

        setCommentSize(postPage);

        return postPage;
    }

    public Page<RetrievePostListResponseElement> searchByContent(String content, int page, int perPage){
        Page<RetrievePostListResponseElement> postPage = postSearchRepository.findByContents(content, PageRequest.of(page, perPage));

        setCommentSize(postPage);

        return postPage;
    }

    public Page<RetrievePostListResponseElement> searchByTitleOrContent(String query, int page, int perPage){
        Page<RetrievePostListResponseElement> postPage = postSearchRepository.findByTitleOrContents(query, PageRequest.of(page, perPage));

        setCommentSize(postPage);

        return postPage;
    }

    public Page<RetrievePostListResponseElement> searchByAuthorName(String authorName, int page, int perPage){
        Page<RetrievePostListResponseElement> postPage = postSearchRepository.findByAuthorName(authorName, PageRequest.of(page, perPage));

        setCommentSize(postPage);

        return postPage;
    }

    public Page<RetrievePostListResponseElement> searchByAll(String query, int page, int perPage){
        Page<RetrievePostListResponseElement> postPage = postSearchRepository.findByAllOptions(query, PageRequest.of(page, perPage));

        setCommentSize(postPage);

        return postPage;
    }


    private void setCommentSize(Page<RetrievePostListResponseElement> postPage){
        postPage.forEach(postDto -> { // TODO : N+1
            int commentSize = commentRepository.countCommentsByPostId(postDto.getPostId());
            postDto.setCommentSize(commentSize);
        });
    }

}
