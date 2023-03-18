package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.Author;
import com.seproject.seboard.domain.model.Comment;
import com.seproject.seboard.domain.model.Post;
import com.seproject.seboard.domain.model.UnnamedComment;
import com.seproject.seboard.domain.repository.AuthorRepository;
import com.seproject.seboard.domain.repository.CommentRepository;
import com.seproject.seboard.domain.repository.PostRepository;
import com.seproject.seboard.domain.repository.UnnamedCommentRepository;
import com.seproject.seboard.dto.CommentDTO;
import com.seproject.seboard.dto.ReplyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommentAppService {

    private final UnnamedCommentRepository unnamedCommentRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    public void createNamedComment(Long userId, Long postId, String contents){
        Author author = findByIdOrThrow(userId, authorRepository, "");
        Post post = findByIdOrThrow(postId,postRepository,"");

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .contents(contents)
                .build();

        commentRepository.save(comment);
    }

    public void createUnnamedComment(Long postId, String contents,String username, String password){
        Post post = findByIdOrThrow(postId,postRepository,"");

        Author author = Author.builder()
                .loginId(Author.ANONYMOUS_LOGIN_ID)
                .name(username)
                .build();

        UnnamedComment comment = UnnamedComment.builder()
                .post(post)
                .author(author)
                .contents(contents)
                .password(password)
                .build();

        unnamedCommentRepository.save(comment);
    }

    public void createNamedReply(Long userId, Long postId, Long commentId, Long taggedCommentId,String contents){
        Post post = findByIdOrThrow(postId,postRepository,"");
        Comment superComment = findByIdOrThrow(commentId,commentRepository,"");
        Comment taggedComment = findByIdOrThrow(taggedCommentId,commentRepository,"");
        Author author = findByIdOrThrow(userId, authorRepository,"");

        Comment reply = Comment.builder()
                .post(post)
                .author(author)
                .superComment(superComment)
                .tag(taggedComment)
                .contents(contents)
                .build();

        commentRepository.save(reply);
    }


    public void createUnnamedReply(Long postId, Long commentId,Long taggedCommentId, String contents,String username, String password){
        Post post = findByIdOrThrow(postId,postRepository,"");

        Author author = Author.builder()
                .loginId(Author.ANONYMOUS_LOGIN_ID)
                .name(username)
                .build();

        Comment superComment = findByIdOrThrow(commentId,commentRepository,"");
        Comment taggedComment = findByIdOrThrow(taggedCommentId,commentRepository,"");

        UnnamedComment unnamedComment = UnnamedComment.builder()
                .post(post)
                .author(author)
                .superComment(superComment)
                .tag(taggedComment)
                .contents(contents)
                .password(password)
                .build();

        unnamedCommentRepository.save(unnamedComment);
    }

    // Retrieve -> Comment만
    //TODO : void -> 변경필요
    public List<CommentDTO.CommentListResponseDTO> retrieveCommentList(Long postId,Long userId){
        //TODO : pagination 고려

        Author requestUser = findByIdOrThrow(userId,authorRepository,"");

        //TODO : JPQL 최적화 고려,
        // 정렬 방법 -> superComment 생성 시간순으로 정렬 -> 자신의 생성 시간순으로 정렬
        // 이렇게 하면 댓글 -> 답글 , 댓글 -> 답글 순으로 정렬 가능, 방법은 JPQL 만들고 결정해야할듯
        List<Comment> comments = commentRepository.findByPostId(postId);
        Collections.sort(comments);

        List<CommentDTO.CommentListResponseDTO> result = new ArrayList<>();

        for (int i = 0; i < comments.size();) {
            Comment superComment = comments.get(i);
            List<Comment> replies = new ArrayList<>();
            i++;
            for (;  i < comments.size(); i++) {
                Comment reply = comments.get(i);
                if(reply.isReply()) replies.add(reply);
                else break;
            }

            List<ReplyDTO.ReplyResponseDTO> repliesDTO = replies.stream().map(reply -> {
                boolean isEditable = reply.isWrittenBy(requestUser);
                return ReplyDTO.ReplyResponseDTO.toDTO(reply,isEditable);
            }).collect(Collectors.toList());
            boolean isEditable = superComment.isWrittenBy(requestUser);
            CommentDTO.CommentListResponseDTO commentDTO = CommentDTO.CommentListResponseDTO.toDTO(superComment,isEditable,repliesDTO);
            result.add(commentDTO);
        }

        return result;

        /*
        return comments.stream().map(comment -> {
            boolean isEditable = comment.isWrittenBy(requestUser);
            List<Comment> replies = commentRepository.findRepliesBySuperCommentId(comment.getSuperComment().getCommentId());
            List<ReplyDTO.ReplyResponseDTO> replyResponseDTOS = replies.stream().map(reply -> {
                boolean isEditableReply = reply.isWrittenBy(requestUser);
                return ReplyDTO.ReplyResponseDTO.toDTO(reply, isEditableReply);
            }).collect(Collectors.toList());
            return CommentDTO.CommentListResponseDTO.toDTO(comment,isEditable,replyResponseDTOS);
        }).collect(Collectors.toList());
        */
    }

    public void changeContentsOfNamed(Long commentId, Long userId, String contents){
        Comment comment = findByIdOrThrow(commentId,commentRepository,"");
        Author requestUser = findByIdOrThrow(userId,authorRepository,"");

        if(comment.isNamed() && comment.isWrittenBy(requestUser)) {
            comment.change(contents);
        }
        //작성자가 다른 경우, 또는 익명 게시글이 선택된 경우
        throw new IllegalArgumentException();
    }

    public void changeContentsOfUnnamed(Long commentId, String password, String contents){
        UnnamedComment unnamedComment = findByIdOrThrow(commentId,unnamedCommentRepository,"");

        if(unnamedComment.checkPassword(password)) {
            unnamedComment.change(contents);
        }

        // 패스워드가 다른 경우
        throw new IllegalArgumentException();
    }

    public void deleteNamedComment(Long commentId, Long userId){
        Comment comment = findByIdOrThrow(commentId,commentRepository,"");
        Author requestUser = findByIdOrThrow(userId,authorRepository,"");

        if (comment.isWrittenBy(requestUser)) {
            commentRepository.delete(comment);
        }

        // 작성자가 다른 경우
        throw new IllegalArgumentException();
    }

    public void deleteUnnamedComment(Long commentId, String password){
        UnnamedComment unnamedComment = findByIdOrThrow(commentId,unnamedCommentRepository,"");

        if (unnamedComment.checkPassword(password)) {
            unnamedCommentRepository.delete(unnamedComment);
        }

        // 패스워드가 다른 경우
        throw new IllegalArgumentException();
    }

    private <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repo, String errorMsg){
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(errorMsg));
    }
    
}
