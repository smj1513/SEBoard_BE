package com.seproject.seboard.application;

import com.seproject.seboard.application.dto.post.PostCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostEditCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostRemoveCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostWriteCommand;
import com.seproject.seboard.domain.model.common.BaseTime;
import com.seproject.seboard.domain.model.exposeOptions.*;
import com.seproject.seboard.domain.model.post.Category;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.Anonymous;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.post.CategoryRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.user.AnonymousRepository;
import com.seproject.seboard.domain.repository.user.BoardUserRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import com.seproject.seboard.oauth2.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.seproject.seboard.application.utils.AppServiceHelper.*;

@Service
@RequiredArgsConstructor
public class PostAppService {
    private final PostRepository postRepository;
    private final BoardUserRepository boardUserRepository;
    private final CategoryRepository categoryRepository;
    private final AnonymousRepository anonymousRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

//    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public void writeUnnamedPost(PostWriteCommand command) { //accID는 체킹되었다고 가정
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

    public void writeNamedPost(PostWriteCommand command) {
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

//    public PostDTO.PostResponseDTO findPost(Long postId, Long accountId){
//        Post post = findByIdOrThrow(postId, postRepository, "");
//        //TODO : member없을 때 로직 추가 필요
//        Member member = memberRepository.findByAccountId(accountId);
//
//        boolean isEditable = false;
//        boolean bookmarked = bookmarkRepository.existsByPostIdAndMemberId(postId, member.getBoardUserId());
//
//        if(post.isWrittenBy(accountId)){ //TODO : 권한 있을때 경우 추가
//            isEditable = true;
//        }
//
//        //TODO : 반환 객체 변경필요하지 않나?
//
//        return PostDTO.PostResponseDTO.toDTO(targetPost,isEditable,bookmarked);
//    }

    public void findPostList(Long categoryId, int page, int size){
//        //TODO : paging 인자, Repository 분리?
//        //TODO : 추후 JPQL써서 개선, categoryId 별로 정리
//        return postRepository.findAll().stream().map(post -> {
//            int commentSize = commentRepository.getCommentSizeByPostId(post.getPostId());
//            return PostDTO.PostListResponseDTO.toDTO(post,commentSize);
//        }).collect(Collectors.toList());
    }

    public void findPinedPostList(Long categoryId){
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

    public void removePost(PostRemoveCommand command) {
        Post post = findByIdOrThrow(command.getPostId(), postRepository, "");

        if(!post.isWrittenBy(command.getAccountId())){ // TODO : 관리자 삭제 경우 추가해야함
            throw new IllegalArgumentException();
        }

        postRepository.deleteById(command.getPostId());
    }
}
