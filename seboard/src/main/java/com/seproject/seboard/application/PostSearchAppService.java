package com.seproject.seboard.application;

import com.seproject.seboard.controller.dto.PaginationResponse;
import com.seproject.seboard.controller.dto.post.PostResponse;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponse;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.repository.Page;
import com.seproject.seboard.domain.repository.PagingInfo;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PostSearchAppService {
    private final PostSearchRepository postSearchRepository;
    private final CommentRepository commentRepository;

    public RetrievePostListResponse searchByTitle(String title, int page, int perPage){
        Page<Post> postPage = postSearchRepository.findByTitle(title, new PagingInfo(page, perPage));

        return getRetrievePostListResponse(postPage);
    }

    public RetrievePostListResponse searchByContent(String content, int page, int perPage){
        Page<Post> postPage = postSearchRepository.findByContent(content, new PagingInfo(page, perPage));

        return getRetrievePostListResponse(postPage);
    }

    public RetrievePostListResponse searchByTitleOrContent(String query, int page, int perPage){
        Page<Post> postPage = postSearchRepository.findByTitleOrContent(query, new PagingInfo(page, perPage));

        return getRetrievePostListResponse(postPage);
    }

    public RetrievePostListResponse searchByAuthorName(String authorName, int page, int perPage){
        Page<Post> postPage = postSearchRepository.findByAuthorName(authorName, new PagingInfo(page, perPage));

        return getRetrievePostListResponse(postPage);
    }

    public RetrievePostListResponse searchByAll(String query, int page, int perPage){
        Page<Post> postPage = postSearchRepository.findByAllOptions(query, new PagingInfo(page, perPage));

        return getRetrievePostListResponse(postPage);
    }

    private RetrievePostListResponse getRetrievePostListResponse(Page<Post> postPage) {
        List<PostResponse.RetrievePostListResponseElement> postDtoList = postPage.getData()
                .stream()
                .map(post -> {
                    int commentSize = commentRepository.countCommentsByPostId(post.getPostId());
                    return PostResponse.RetrievePostListResponseElement.toDTO(post, commentSize);
                }).collect(Collectors.toList());

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .currentPage(postPage.getCurPage())
                .contentSize(postPage.getTotalSize())
                .perPage(postPage.getPerPage())
                .lastPage(postPage.getLastPage())
                .build();

        return RetrievePostListResponse.toDTO(postDtoList, paginationResponse);
    }


}
