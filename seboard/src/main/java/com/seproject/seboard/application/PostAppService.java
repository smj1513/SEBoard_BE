package com.seproject.seboard.application;

import com.seproject.seboard.application.dto.post.PostCommand.PostEditCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostListFindCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostWriteCommand;
import com.seproject.seboard.controller.dto.PaginationResponse;
import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.exposeOptions.*;
import com.seproject.seboard.domain.model.post.Category;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.Anonymous;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.post.CategoryRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import com.seproject.seboard.domain.repository.user.AnonymousRepository;
import com.seproject.seboard.domain.repository.user.BoardUserRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import com.seproject.oauth2.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.seproject.seboard.application.utils.AppServiceHelper.*;
import static com.seproject.seboard.controller.dto.post.PostResponse.*;

@Service
@RequiredArgsConstructor
public class PostAppService {
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final CategoryRepository categoryRepository;
    private final AnonymousRepository anonymousRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;

    public void writePost(PostWriteCommand command){
        if(command.isAnonymous()){
            writeUnnamedPost(command);
        }else{
            writeNamedPost(command);
        }
    }

    @Transactional
    protected void writeUnnamedPost(PostWriteCommand command) { //accID는 체킹되었다고 가정
        Category category = findByIdOrThrow(command.getCategoryId(), categoryRepository, "");

        Anonymous anonymous = Anonymous.builder()
                .name("익명") //TODO : 익명 이름 다양하게?
                .accountId(command.getAccountId())
                .build();

        //TODO : attachment 로직 추가
        Post post = Post.builder()
                .title(command.getTitle())
                .contents(command.getContents())
                .category(category)
                .author(anonymous)
                .baseTime(BaseTime.now())
                .pined(command.isPined())
                //TODO : 좀더 유연하게 변경?
                .exposeOption(ExposeOption.of(command.getExposeState(), command.getPrivatePassword()))
                .build();

        anonymousRepository.save(anonymous);
        postRepository.save(post);
    }

    protected void writeNamedPost(PostWriteCommand command) {
        Member member = memberRepository.findByAccountId(command.getAccountId());

        if (member == null) {
            //TODO : member 생성 로직 호출
        } else {
            Category category = findByIdOrThrow(command.getCategoryId(), categoryRepository, "");
            Post post = Post.builder()
                    .title(command.getTitle())
                    .contents(command.getContents())
                    .category(category)
                    .author(member)
                    .baseTime(BaseTime.now())
                    .pined(command.isPined())
                    .exposeOption(ExposeOption.of(command.getExposeState(), command.getPrivatePassword()))
                    .build();

            postRepository.save(post);
        }
    }

    //TODO : paging 처리해야함
    public RetrievePostListResponse findPostList(PostListFindCommand command, boolean pinedOption){
//        Page<Post> postPage = null;
//
//        if(pinedOption){
//            postPage = postSearchRepository.findPinedPostByCategoryId(command.getCategoryId(), PageRequest.of(command.getPage(), command.getSize()));
//        }else{
//            postPage = postSearchRepository.findByCategoryId(command.getCategoryId(), PageRequest.of(command.getPage(), command.getSize())));
//        }
//
//        List<RetrievePostListResponseElement> postDtoList = postPage .stream()
//            .map(post -> {
//                int commentSize = commentRepository.countCommentsByPostId(post.getPostId());
//                return RetrievePostListResponseElement.toDTO(post, commentSize);
//            }).collect(Collectors.toList());
//
//        PaginationResponse paginationResponse = PaginationResponse.builder()
//                .currentPage(postPage.getCurPage())
//                .contentSize(postPage.getTotalSize())
//                .perPage(postPage.getPerPage())
//                .lastPage(postPage.getLastPage())
//                .build();
//
//        return RetrievePostListResponse.toDTO(postDtoList, paginationResponse);
        return null;
    }

    public void editPost(PostEditCommand command) {
        Post post = findByIdOrThrow(command.getPostId(), postRepository, "");

        if(!post.isWrittenBy(command.getAccountId())){
            throw new IllegalArgumentException();
        }

        post.changeTitle(command.getTitle());
        post.changeContents(command.getTitle());
        post.changePin(command.isPined());
        post.changeExposeOption(ExposeOption.of(command.getExposeState(), command.getPrivatePassword()));

        //TODO : category 권한 체킹 필요
        Category category = findByIdOrThrow(command.getCategoryId(), categoryRepository, "");
        post.changeCategory(category);
    }

    public void removePost(Long postId, Long accountId) {
        Post post = findByIdOrThrow(postId, postRepository, "");

        if(!post.isWrittenBy(accountId)){ // TODO : 관리자 삭제 경우 추가해야함
            throw new IllegalArgumentException();
        }

        postRepository.deleteById(postId);
    }
}
