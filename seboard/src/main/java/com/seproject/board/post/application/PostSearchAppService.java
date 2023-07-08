package com.seproject.board.post.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.account.domain.repository.AccountRepository;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.board.post.controller.dto.PostResponse.RetrievePostDetailResponse;
import com.seproject.board.post.controller.dto.PostResponse.RetrievePostListResponseElement;
import com.seproject.board.menu.domain.BoardMenu;
import com.seproject.board.post.domain.model.Post;
import com.seproject.board.post.domain.model.exposeOptions.ExposeState;
import com.seproject.member.domain.Member;
import com.seproject.board.menu.domain.repository.BoardMenuRepository;
import com.seproject.board.comment.domain.repository.CommentRepository;
import com.seproject.board.comment.domain.repository.CommentSearchRepository;
import com.seproject.board.comment.domain.repository.ReplyRepository;
import com.seproject.board.post.domain.repository.BookmarkRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.board.post.domain.repository.PostSearchRepository;
import com.seproject.member.domain.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class PostSearchAppService {
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final CommentSearchRepository commentSearchRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final AccountRepository accountRepository;
    private final BoardMenuRepository boardMenuRepository;


    @Transactional
    public RetrievePostDetailResponse findPrivacyPost(Long postId, String password){
        //TODO : 추후 변경 필요
        Account account = SecurityUtils.getAccount().orElse(null);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        RetrievePostDetailResponse postDetailResponse = postSearchRepository.findPostDetailById(postId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        if(post.getExposeOption().getExposeState()!=ExposeState.PRIVACY){
            return findPostDetail(postId);
        }

        if(!post.checkPassword(password)){
            throw new InvalidAuthorizationException(ErrorCode.INCORRECT_POST_PASSWORD);
        }

        //TODO : member없을 때 로직 추가 필요
        //TODO : 관리자 권한일 때 editable 로직 추가필요
        boolean isEditable = false;
        boolean isBookmarked = false;

        if(account!=null) {
            Member member = memberRepository.findByAccountId(account.getAccountId())
                    .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER));

            isBookmarked = bookmarkRepository.existsByPostIdAndMemberId(postId, member.getBoardUserId());
            isEditable = post.isWrittenBy(account.getAccountId()) || post.getCategory().manageable(account.getAuthorities());
        }

        postDetailResponse.setEditable(isEditable);
        postDetailResponse.setBookmarked(isBookmarked);

        post.increaseViews();

        return postDetailResponse;
    }

    @Transactional
    public RetrievePostDetailResponse findPostDetail(Long postId){
        //TODO : 변경 필요
        Account account = SecurityUtils.getAccount().orElse(null);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        RetrievePostDetailResponse postDetailResponse = postSearchRepository.findPostDetailById(postId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        //TODO : 관리자 권한일 때 editable 로직 추가필요
        boolean isEditable = false;
        boolean isBookmarked = false;

        if(account!=null) {
            Member member = memberRepository.findByAccountId(account.getAccountId())
                    .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER));

            isBookmarked = bookmarkRepository.existsByPostIdAndMemberId(postId, member.getBoardUserId());
            isEditable = post.isWrittenBy(account.getAccountId()) || post.getCategory().manageable(account.getAuthorities());
        }

        postDetailResponse.setEditable(isEditable);
        postDetailResponse.setBookmarked(isBookmarked);

        if(post.getExposeOption().getExposeState()== ExposeState.PRIVACY){
            if(account!=null && (post.isWrittenBy(account.getAccountId()) || post.getCategory().manageable(account.getAuthorities()))){
                post.increaseViews();
                return postDetailResponse;
            }else{
                throw new InvalidAuthorizationException(ErrorCode.NOT_POST_AUTHOR);
            }
        }else if(post.getExposeOption().getExposeState() == ExposeState.KUMOH){
            if(account!=null){
                Collection<? extends GrantedAuthority> authorities = SecurityUtils.getAuthorities();
                boolean isKumoh = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_KUMOH"));
                if(isKumoh || post.getCategory().manageable(account.getAuthorities())){
                    post.increaseViews();
                    return postDetailResponse;
                }else{
                    throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
                }
            }else{
                throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
            }
        }


        post.increaseViews();
        return postDetailResponse;
    }

    public List<RetrievePostListResponseElement> findPinedPostList(Long categoryId){
        BoardMenu boardMenu = boardMenuRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));

        List<RetrievePostListResponseElement> postPage;

        if(boardMenu.exposable(new ArrayList<>())){
            postPage = postSearchRepository.findPinedPostByCategoryId(categoryId);

            postPage.forEach(postDto -> {
                int commentSize = commentRepository.countCommentsByPostId(postDto.getPostId());
                postDto.setCommentSize(commentSize);
            });
        }else{ //최소 로그인을 해야만 볼 수 있다
            Account account = SecurityUtils.getAccount().orElseThrow(()-> new NoSuchResourceException(ErrorCode.NOT_LOGIN));

            if(!boardMenu.exposable(account.getAuthorities())){
                throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
            }

            postPage = postSearchRepository.findPinedPostByCategoryId(categoryId);

            postPage.forEach(postDto -> {
                int commentSize = commentRepository.countCommentsByPostId(postDto.getPostId());
                int replySize = commentSearchRepository.countReplyByPostId(postDto.getPostId());
                postDto.setCommentSize(commentSize+replySize);
            });
        }

        return postPage;
    }

    public Page<RetrievePostListResponseElement> findPostList(Long categoryId, int page, int size){
        BoardMenu boardMenu = boardMenuRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_CATEGORY));

        Page<RetrievePostListResponseElement> postPage;

        //TODO : 리팩토링 필요
        //노출 옵션이 ALL인지 확인(로그인 안해도 볼 수 있는지)
        if(boardMenu.exposable(new ArrayList<>())){
            postPage = postSearchRepository.findPostByCategoryId(categoryId, PageRequest.of(page, size));
            setCommentSize(postPage);
        }else{ //최소 로그인을 해야만 볼 수 있다
            Account account = SecurityUtils.getAccount().orElseThrow(()-> new NoSuchResourceException(ErrorCode.NOT_LOGIN));

            if(!boardMenu.exposable(account.getAuthorities())){
                throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
            }


            postPage = postSearchRepository.findPostByCategoryId(categoryId, PageRequest.of(page, size));
            setCommentSize(postPage);
        }

        return postPage;
    }

    public Page<RetrievePostListResponseElement> searchByTitle(String title, int page, int perPage){
        Page<RetrievePostListResponseElement> postPage = postSearchRepository.findByTitle(title, PageRequest.of(page, perPage));

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
        postPage.forEach(postDto -> {
            int commentSize = commentRepository.countCommentsByPostId(postDto.getPostId());
            postDto.setCommentSize(commentSize);
        });
    }

}
