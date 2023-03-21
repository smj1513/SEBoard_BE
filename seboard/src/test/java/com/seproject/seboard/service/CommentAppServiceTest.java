package com.seproject.seboard.service;

import com.seproject.seboard.application.CommentAppService;
import com.seproject.seboard.domain.model.*;
import com.seproject.seboard.domain.repository.AuthorRepository;
import com.seproject.seboard.domain.repository.CommentRepository;
import com.seproject.seboard.domain.repository.PostRepository;
import com.seproject.seboard.domain.repository.UnnamedCommentRepository;
import com.seproject.seboard.dto.CommentDTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentAppServiceTest {


    private CommentRepository commentRepository;
    private UnnamedCommentRepository unnamedCommentRepository;
    private AuthorRepository authorRepository;
    private PostRepository postRepository;

    private Author createAuthor(Long authorId,String loginId,String name) {
        Author author = Author.builder()
                .authorId(authorId)
                .loginId(loginId)
                .name(name)
                .build();

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        return author;
    }

    private Comment createMockComment(Long commentId, String contents,int year, int month, int day, Author author) {
        Comment comment = Comment.builder()
                .contents(contents)
                .commentId(commentId)
                .author(author)
                .baseTime(new BaseTime(LocalDateTime.of(year, month, day, 0, 0), null))
                .build();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        return comment;
    }

    private UnnamedComment createMockUnnamedComment(Long commentId, String contents,int year, int month, int day, Author author,String password) {
        UnnamedComment comment = UnnamedComment.builder()
                .commentId(commentId)
                .contents(contents)
                .author(author)
                .baseTime(new BaseTime(LocalDateTime.of(year, month, day, 0, 0), null))
                .password(password)
                .build();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        return comment;
    }

    private Comment createMockReply(Long commentId, Comment superComment,Comment taggedComment,Post post, int year, int month, int day, Author author) {
        Comment reply = Comment.builder()
                .commentId(commentId)
                .superComment(superComment)
                .tag(taggedComment)
                .author(author)
                .post(post)
                .baseTime(new BaseTime(LocalDateTime.of(year, month, day, 0, 0), null))
                .build();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(reply));
        return reply;
    }

    private Post createMockPost(Long postId,Author author,Category category,String title,String contents,boolean pined){
        Post post = Post.builder()
                .postId(postId)
                .author(author)
                .category(category)
                .title(title)
                .contents(contents)
                .pined(pined)
                .views(0)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        return post;
    }

    @BeforeAll
    public void init() {
        commentRepository = mock(CommentRepository.class);
        authorRepository = mock(AuthorRepository.class);
        postRepository = mock(PostRepository.class);
        unnamedCommentRepository = mock(UnnamedCommentRepository.class);

        Author firstAuthor = createAuthor(1L, "hong", "홍길동");
        Author secondAuthor = createAuthor(2L, "kim", "김민종");
        Author thirdAuthor = createAuthor(3L, "lee", "이순신");

        Post post = createMockPost(1L,null,null,null,null,true);

        Comment first = createMockComment(1L,"comment1",3022,11,13,firstAuthor);
            Comment firstReply = createMockReply(4L,first,first,post,2021,11,4,secondAuthor);
            Comment secondReply = createMockReply(5L,first,firstReply,post,300,2,11,thirdAuthor);
        Comment second = createMockComment(2L,"comment2",1021,11,13,secondAuthor);
            Comment thirdReply = createMockReply(6L,second,second,post,1110,4,13,thirdAuthor);
        Comment third = createMockComment(3L,"comment3",2000,11,13,firstAuthor);

        List<Comment> stub = List.of(first, second, third, firstReply, secondReply, thirdReply);

        when(commentRepository.findByPostId(1L)).thenReturn(new ArrayList<>(stub));
    }
    @Test
    @DisplayName("게시글 상세 조회시 게시글에 달린 댓글들이 정상적으로 조회되는지 검사하는 기능")
    void retrieveCommentListTest() {
        CommentAppService commentAppService = new CommentAppService(null,commentRepository,null,authorRepository);
        List<CommentDTO.CommentListResponseDTO> commentListResponseDTOS = commentAppService.retrieveCommentList(1L, 1L);
        List<Integer> ans = List.of(1,0,2);

        for (int i = 0; i < commentListResponseDTOS.size(); i++) {
            CommentDTO.CommentListResponseDTO commentListResponseDTO = commentListResponseDTOS.get(i);
            System.out.println(commentListResponseDTO);
            commentListResponseDTO.getReplies().forEach(reply -> {
                System.out.println("---> " + reply);
            });
            System.out.println("---------------------------");
        }

        for (int i = 0; i < 3; i++) {
            CommentDTO.CommentListResponseDTO commentListResponseDTO = commentListResponseDTOS.get(i);
            Assertions.assertEquals(commentListResponseDTO.getReplies().size(),ans.get(i));
        }

    }

    @Test
    @DisplayName("답글 작성 테스트")
    void writeNamedReplyTests() {
        CommentAppService commentAppService = new CommentAppService(null,commentRepository,postRepository,authorRepository);

        Long postId = 2L;
        Post post = createMockPost(postId,null,null,null,null,false);

        Long superCommentId = 2L;
        Comment superComment = createMockComment(superCommentId,"superComment",2012,10,10,null);

        Long taggedCommentId = 3L;
        Comment taggedComment = createMockComment(superCommentId,"taggedComment",2013,11,10,null);

        Long userId = 4L;
        Author author = createAuthor(userId,null,null);

        commentAppService.createNamedReply(userId,postId,superCommentId,taggedCommentId,"댓글 내용");
    }

    @Test
    @DisplayName("답글 작성 실패 - 사용자 정보가 없는경우 테스트")
    void writeNamedReplyNoAuthorTests() {
        CommentAppService commentAppService = new CommentAppService(null,commentRepository,postRepository,authorRepository);

        Long postId = 2L;
        Post post = createMockPost(postId,null,null,null,null,false);

        Long superCommentId = 2L;
        Comment superComment = createMockComment(superCommentId,"superComment",2012,10,10,null);

        Long taggedCommentId = 3L;
        Comment taggedComment = createMockComment(superCommentId,"taggedComment",2013,11,10,null);

        Long userId = 123414L;

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            commentAppService.createNamedReply(userId,postId,superCommentId,taggedCommentId,"댓글 내용");
        });
    }

    @Test
    @DisplayName("답글 수정 테스트")
    void updateNamedReplyTests() {
        CommentAppService commentAppService = new CommentAppService(null,commentRepository,postRepository,authorRepository);

        Long userId = 4L;
        Author author = createAuthor(userId,null,null);

        Long postId = 2L;
        Post post = createMockPost(postId,author,null,null,null,false);

        Long superCommentId = 2L;
        Comment superComment = createMockComment(superCommentId,"superComment",2012,10,10,null);

        Long replyId = 3L;
        String newContents = "new Contents";

        Comment reply = createMockReply(replyId,superComment,superComment,post,2013,11,10,author);

        Assertions.assertTrue(reply.isWrittenBy(author));
        Assertions.assertTrue(reply.isNamed());

        commentAppService.changeContentsOfNamed(replyId,userId,newContents);
        String contents = reply.getContents();

        Assertions.assertEquals(contents,newContents);
    }

    @Test
    @DisplayName("답글 수정 테스트 - 익명 답글이 선택된 경우")
    void updateNamedReplyWhenUnnamedSelectedTests () {
        CommentAppService commentAppService = new CommentAppService(null,commentRepository,postRepository,authorRepository);
        Long userId = 4L;
        Long replyId = 4L;
        String newContents = "new Contents";

        Author author = createAuthor(userId,null,null);

        UnnamedComment reply = createMockUnnamedComment(replyId,"it is reply",2012,1,13,null,"originPassword");

        when(commentRepository.findById(replyId)).thenReturn(Optional.of(reply));

        Assertions.assertFalse(reply.isNamed());
        Assertions.assertThrows(IllegalArgumentException.class, () -> commentAppService.changeContentsOfNamed(replyId,userId,newContents));
        String contents = reply.getContents();
        Assertions.assertNotEquals(contents,newContents);
    }
}
