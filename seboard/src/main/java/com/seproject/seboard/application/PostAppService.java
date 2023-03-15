package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.Post;
import com.seproject.seboard.domain.model.author.Anonymous;
import com.seproject.seboard.domain.model.author.Member;
import com.seproject.seboard.domain.repository.AnonymousRepository;
import com.seproject.seboard.domain.repository.MemberRepository;
import com.seproject.seboard.domain.repository.PostRepository;
import com.seproject.seboard.domain.service.PostService;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;

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
        Member member = findMemberById(userId);

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
        Post targetPost = findPostById(postId);
        Member targetMember = findMemberById(userId);

        //작성한 게시글의 작성자가 존재하지 않는 경우 TODO : message
        if(!targetPost.isSameAuthor(targetMember)) {
            throw new IllegalArgumentException();
        }

        postRepository.deleteById(postId);
    }

    public void deleteUnnamedPost(Long postId, Long userId, String password) {
        // 권한 체크 -> 메소드 보안 체크 가능함 : AOP로 처리
        // 시큐리티 클라우드 적용 방법 -> 게이트웨이에 적용 가능


        Post targetPost = findPostById(postId);
        Anonymous targetAnonymous = findAnonymousById(userId);

        //작성한 게시글의 작성자가 서로 다른 경우 TODO : message
        if(!targetPost.isSameAuthor(targetAnonymous)) {
            throw new IllegalArgumentException();
        }
        // 비밀번호 확인
        if(!targetAnonymous.checkPassword(password)) {
            throw new IllegalArgumentException();
        }

        postRepository.deleteById(postId);
    }

    // 게시물을 찾고 존재하지 않으면 예외를 전달
    private Post findPostById(Long postId){
        Optional<Post> post = postRepository.findById(postId);

        //게시물이 존재하지 않는 경우 TODO : message
        if(post.isEmpty()) {
            throw new NoSuchElementException();
        }

        Post targetPost = post.get();
        return targetPost;
    }


    //사용자를 찾고 존재하지 않으면 예외를 전달하는 함수 -> 하나로 함치는 방법 고려
    private Member findMemberById(Long memberId){
        Optional<Member> foundMember = memberRepository.findById(memberId);

        //작성한 게시글의 작성자가 존재하지 않는 경우 TODO : message
        if(foundMember.isEmpty()) {
            throw new NoSuchElementException();
        }

        Member targetMember = foundMember.get();
        return targetMember;
    }

    private Anonymous findAnonymousById(Long anonymousId){
        Optional<Anonymous> foundAnonymous = anonymousRepository.findById(anonymousId);

        //작성한 게시글의 작성자가 존재하지 않는 경우 TODO : message
        if(foundAnonymous.isEmpty()) {
            throw new NoSuchElementException();
        }

        Anonymous targetAnonymous = foundAnonymous.get();
        return targetAnonymous;
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
