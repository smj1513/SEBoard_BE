package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.Post;
import com.seproject.seboard.domain.model.author.Anonymous;
import com.seproject.seboard.domain.model.author.Author;
import com.seproject.seboard.domain.model.author.Member;
import com.seproject.seboard.domain.repository.AnonymousRepository;
import com.seproject.seboard.domain.repository.MemberRepository;
import com.seproject.seboard.domain.repository.PostRepository;
import com.seproject.seboard.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class PostAppService {
    //controller validation check -> 타입 맞는지, 공백 and null 체크
    //나머지는 application 아래에서
    private final PostService postService;

    private final PostRepository postRepository;
    // Repository 하위 타입 분할
    private final AnonymousRepository anonymousRepository;
    private final MemberRepository memberRepository;

    public void createUnnamedPost(String title, String contents, Long categoryId, String username, String password){
        validatePost(title,contents,categoryId);

        Anonymous anonymous = Anonymous.builder()
                .name(username)
                .password(password)
                .build();

        anonymousRepository.save(anonymous);
        Post post = Post.builder()
                .title(title)
                .contents(contents)
                .categoryId(categoryId)
                .author(anonymous)
                .build();

        postRepository.save(post);
    }

    public void createNamedPost(String title, String contents, Long categoryId, Long userId){
        validatePost(title,contents,categoryId);
        Member member = findByIdOrThrow(userId, memberRepository, "");

        Post post = Post.builder()
                .title(title)
                .contents(contents)
                .categoryId(categoryId)
                .author(member) //멤버 등록
                .build();

        postRepository.save(post);
    }


    public void deleteNamedPost(Long postId, Long userId ) {
        // 권한 체크 -> 메소드 보안 체크 가능함 : AOP로 처리
        // 시큐리티 클라우드 적용 방법 -> 게이트웨이에 적용 가능
        Post targetPost = findByIdOrThrow(postId, postRepository, "");
        Member author = findByIdOrThrow(userId, memberRepository, "");

        //작성한 게시글의 작성자가 존재하지 않는 경우 TODO : message
        if(!targetPost.isSameAuthor(author)) {
            throw new IllegalArgumentException();
        }

        postRepository.deleteById(postId);
    }

    public void deleteUnnamedPost(Long postId, Long userId, String password) {
        // 권한 체크 -> 메소드 보안 체크 가능함 : AOP로 처리
        // 시큐리티 클라우드 적용 방법 -> 게이트웨이에 적용 가능


        Post targetPost = findByIdOrThrow(postId, postRepository, "");
        Anonymous author = findByIdOrThrow(userId, anonymousRepository, "");

        //작성한 게시글의 작성자가 서로 다른 경우 TODO : message
        if(!targetPost.isSameAuthor(author)) {
            throw new IllegalArgumentException();
        }
        // 비밀번호 확인
        if(!author.checkPassword(password)) {
            throw new IllegalArgumentException();
        }

        postRepository.deleteById(postId);
    }

    // 게시글 수정
    public void updateNamedPost(String title,String contents,Long categoryId,Long postId,Long userId){
        validatePost(title,contents,categoryId);

        //게시물 및 요청자 id를 이용한 대상 조회
        Post targetPost = findByIdOrThrow(postId, postRepository, "");
        Member author = findByIdOrThrow(userId, memberRepository, "");

        // 동일 인물인지 검사
        boolean isAuthor = targetPost.isSameAuthor(author);
        if(!isAuthor) {
            throw new IllegalArgumentException();
        }
        // 게시글 업데이트
        targetPost.update(title,contents,categoryId);
    }

    public void updateUnnamedPost(String title,String contents,Long categoryId,Long postId,Long userId,String password){
        validatePost(title,contents,categoryId);

        //게시물 및 요청자 id를 이용한 대상 조회
        Post targetPost = findByIdOrThrow(postId, postRepository, "");
        Anonymous author = findByIdOrThrow(userId, anonymousRepository,"");

        // 동일 인물인지 검사
        boolean isAuthor = targetPost.isSameAuthor(author);
        if(!isAuthor) {
            throw new IllegalArgumentException();
        }

        //비밀번호 검사
        if(!author.checkPassword(password)) {
            throw new IllegalArgumentException();
        }

        // 게시글 업데이트 -> 익명일때 비밀번호 수정도 필요한가?
        targetPost.update(title,contents,categoryId);
    }

    private <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repo, String errorMsg){
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(errorMsg));
    }

    private void validatePost(String title,String contents,Long categoryId){

        //카테고리 존재하는지 확인 TODO : message 추가
        if(!postService.existCategory(categoryId)) {
            throw new NoSuchElementException();
        }

        //제목 길이 초과여부 확인
        if(!postService.isValidTitle(title)) {
            throw new IllegalArgumentException();
        }

        //본문 길이 초과여부 확인
       if(!postService.isValidContents(contents)) {
            throw new IllegalArgumentException();
        }

    }

}
