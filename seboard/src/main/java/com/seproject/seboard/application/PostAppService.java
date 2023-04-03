package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.common.BaseTime;
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

    //
    @Transactional
    public void writeUnnamedPost(String title, String contents, Long categoryId, boolean pined, Long accountId) { //accID는 체킹되었다고 가정
        Category category = findByIdOrThrow(categoryId, categoryRepository, "");

        Anonymous anonymous = Anonymous.builder()
                .name("익명") //TODO : 익명 이름 다양하게?
                .accountId(accountId)
                .build();

        //TODO : expose option 로직 추가
        Post post = Post.builder()
                .title(title)
                .contents(contents)
                .category(category)
                .author(anonymous)
                .baseTime(BaseTime.now())
                .pined(pined)
                .build();

        anonymousRepository.save(anonymous);
        postRepository.save(post);
    }

    public void writeNamedPost(String title, String contents, Long categoryId, boolean pined, Long accountId) {
        Member member = memberRepository.findByAccountId(accountId);

        if (member == null) {
            //TODO : member 생성 로직 호출
        } else {
            Category category = findByIdOrThrow(categoryId, categoryRepository, "");
            //TODO : expose option 로직 추가
            Post post = Post.builder()
                    .title(title)
                    .contents(contents)
                    .category(category)
                    .author(member)
                    .baseTime(BaseTime.now())
                    .pined(pined)
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

    public void editPost(String title, String contents, Long categoryId, Long postId, boolean pined, Long accountId) {
        Post post = findByIdOrThrow(postId, postRepository, "");

        if(!post.isWrittenBy(accountId)){
            throw new IllegalArgumentException();
        }

        post.changeTitle(title);
        post.changeContents(contents);
        post.changePin(pined);


        //TODO : expose option 로직 추가
        //TODO : 카테고리 변경 추가 필요
        Category category = findByIdOrThrow(categoryId, categoryRepository, "");
    }

    public void removePost(Long postId, Long accountId){
        Post post = findByIdOrThrow(postId, postRepository, "");

        if(!post.isWrittenBy(accountId)){ // TODO : 관리자 삭제 경우 추가해야함
            throw new IllegalArgumentException();
        }

        postRepository.deleteById(postId);
    }

//
//    public void deleteNamedPost(Long postId, Long userId ) {
//        // 권한 체크 -> 메소드 보안 체크 가능함 : AOP로 처리
//        // 시큐리티 클라우드 적용 방법 -> 게이트웨이에 적용 가능
//        Post targetPost = findByIdOrThrow(postId, postRepository, "");
//        User user = findByIdOrThrow(userId, userRepository, "");
//
//        //작성한 게시글의 작성자가 존재하지 않는 경우 TODO : message
//        if(!targetPost.isWrittenBy(user)) {
//            throw new IllegalArgumentException();
//        }
//
//        postRepository.deleteById(postId);
//    }
//
//    public void deleteUnnamedPost(Long postId, String password) {
//        // 권한 체크 -> 메소드 보안 체크 가능함 : AOP로 처리
//        // 시큐리티 클라우드 적용 방법 -> 게이트웨이에 적용 가능
//        UnnamedPost targetPost = findByIdOrThrow(postId, unnamedPostRepository, "");
//
//        // 비밀번호 확인
//        if(!targetPost.checkPassword(password)) {
//            throw new IllegalArgumentException();
//        }
//
//        postRepository.deleteById(postId);
//    }
//


}
