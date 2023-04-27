package com.seproject.seboard.application;

import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostDetailResponse;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class PostSearchAppService {
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    public RetrievePostDetailResponse findPostDetail(Long postId, Long accountId){
        Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
        RetrievePostDetailResponse postDetailResponse =
                postSearchRepository.findPostDetailById(postId).orElseThrow(NoSuchElementException::new);
        //TODO : member없을 때 로직 추가 필요
        //TODO : 관리자 권한일 때 editable 로직 추가필요
        boolean isEditable = false;
        boolean isBookmarked = false;

        if(accountId!=null){
            Member member = memberRepository.findByAccountId(accountId).orElseThrow(NoSuchElementException::new);

            isEditable = post.isWrittenBy(accountId);
            isBookmarked = bookmarkRepository.existsByPostIdAndMemberId(postId, member.getBoardUserId());
        }

        postDetailResponse.setEditable(isEditable);
        postDetailResponse.setBookmarked(isBookmarked);

        return postDetailResponse;
    }

//    public RetrievePostListResponse searchByTitle(String title, int page, int perPage){
//        Page<Post> postPage = postSearchRepository.findByTitle(title, new PagingInfo(page, perPage));
//
//        return getRetrievePostListResponse(postPage);
//    }
//
//    public RetrievePostListResponse searchByContent(String content, int page, int perPage){
//        Page<Post> postPage = postSearchRepository.findByContent(content, new PagingInfo(page, perPage));
//
//        return getRetrievePostListResponse(postPage);
//    }
//
//    public RetrievePostListResponse searchByTitleOrContent(String query, int page, int perPage){
//        Page<Post> postPage = postSearchRepository.findByTitleOrContent(query, new PagingInfo(page, perPage));
//
//        return getRetrievePostListResponse(postPage);
//    }
//
//    public RetrievePostListResponse searchByAuthorName(String authorName, int page, int perPage){
//        Page<Post> postPage = postSearchRepository.findByAuthorName(authorName, new PagingInfo(page, perPage));
//
//        return getRetrievePostListResponse(postPage);
//    }
//
//    public RetrievePostListResponse searchByAll(String query, int page, int perPage){
//        Page<Post> postPage = postSearchRepository.findByAllOptions(query, new PagingInfo(page, perPage));
//
//        return getRetrievePostListResponse(postPage);
//    }
//
//    private RetrievePostListResponse getRetrievePostListResponse(Page<Post> postPage) {
//        List<PostResponse.RetrievePostListResponseElement> postDtoList = postPage.getData()
//                .stream()
//                .map(post -> {
//                    int commentSize = commentRepository.countCommentsByPostId(post.getPostId());
//                    return PostResponse.RetrievePostListResponseElement.toDTO(post, commentSize);
//                }).collect(Collectors.toList());
//
//        PaginationResponse paginationResponse = PaginationResponse.builder()
//                .currentPage(postPage.getCurPage())
//                .contentSize(postPage.getTotalSize())
//                .perPage(postPage.getPerPage())
//                .lastPage(postPage.getLastPage())
//                .build();
//
//        return RetrievePostListResponse.toDTO(postDtoList, paginationResponse);
//    }
//

}
