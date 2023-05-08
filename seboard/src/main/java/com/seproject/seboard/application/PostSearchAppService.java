package com.seproject.seboard.application;

import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostDetailResponse;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponseElement;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.post.exposeOptions.ExposeState;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import com.seproject.seboard.domain.repository.comment.ReplyRepository;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    public RetrievePostDetailResponse findPrivacyPost(Long postId, String password, Long accountId){
        Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
        RetrievePostDetailResponse postDetailResponse =
                postSearchRepository.findPostDetailById(postId).orElseThrow(NoSuchElementException::new);

        if(post.getExposeOption().getExposeState()!=ExposeState.PRIVACY){
            return findPostDetail(postId, accountId);
        }

        if(!post.checkPassword(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //TODO : member없을 때 로직 추가 필요
        //TODO : 관리자 권한일 때 editable 로직 추가필요
        boolean isEditable = false;
        boolean isBookmarked = false;

        if(accountId!=null) {
            Member member = memberRepository.findByAccountId(accountId).orElseThrow(NoSuchElementException::new);

            isEditable = post.isWrittenBy(accountId);
            isBookmarked = bookmarkRepository.existsByPostIdAndMemberId(postId, member.getBoardUserId());
        }

        postDetailResponse.setEditable(isEditable);
        postDetailResponse.setBookmarked(isBookmarked);

        return postDetailResponse;

    }

    public RetrievePostDetailResponse findPostDetail(Long postId, Long accountId){
        Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
        RetrievePostDetailResponse postDetailResponse =
                postSearchRepository.findPostDetailById(postId).orElseThrow(NoSuchElementException::new);
        //TODO : member없을 때 로직 추가 필요
        //TODO : 관리자 권한일 때 editable 로직 추가필요
        boolean isEditable = false;
        boolean isBookmarked = false;

        if(accountId!=null) {
            Member member = memberRepository.findByAccountId(accountId).orElseThrow(NoSuchElementException::new);

            isEditable = post.isWrittenBy(accountId);
            isBookmarked = bookmarkRepository.existsByPostIdAndMemberId(postId, member.getBoardUserId());

        }

        postDetailResponse.setEditable(isEditable);
        postDetailResponse.setBookmarked(isBookmarked);

        if(post.getExposeOption().getExposeState()== ExposeState.PRIVACY){
            if(accountId!=null && post.isWrittenBy(accountId)){
                return postDetailResponse;
            }else{
                throw new IllegalArgumentException("해당 게시글은 비공개 게시글입니다. 비밀번호를 입력해주세요");
            }
        }

        return postDetailResponse;

    }

    public List<RetrievePostListResponseElement> findPinedPostList(Long categoryId){
        List<RetrievePostListResponseElement> pinedPosts = postSearchRepository.findPinedPostByCategoryId(categoryId);

        pinedPosts.forEach(postDto -> {
            int commentSize = commentRepository.countCommentsByPostId(postDto.getPostId());
            int replySize = commentSearchRepository.countReplyByPostId(postDto.getPostId());
            postDto.setCommentSize(commentSize+replySize);
        });

        return pinedPosts;
    }
    //TODO : paging 처리해야함
    public Page<RetrievePostListResponseElement> findPostList(Long categoryId, int page, int size){
        Page<RetrievePostListResponseElement> postPage;

        postPage = postSearchRepository.findPostByCategoryId(categoryId, PageRequest.of(page, size));

        setCommentSize(postPage);

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
            int replySize = commentSearchRepository.countReplyByPostId(postDto.getPostId());
            postDto.setCommentSize(commentSize+replySize);
        });
    }

}
