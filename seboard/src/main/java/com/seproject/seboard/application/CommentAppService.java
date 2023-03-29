package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.user.User;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.repository.user.UserRepository;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentAppService {
//
//    private final CommentRepository commentRepository;
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//
//    public void createNamedComment(Long userId, Long postId, String contents){
//        User user = findByIdOrThrow(userId, userRepository, "");
//        Post post = findByIdOrThrow(postId,postRepository,"");
//
//        AbstractComment abstractComment = AbstractComment.builder()
//                .post(post)
//                .user(user)
//                .contents(contents)
//                .build();
//
//        commentRepository.save(abstractComment);
//    }
//
//    public void createUnnamedComment(Long postId, String contents,String username, String password){
//        Post post = findByIdOrThrow(postId,postRepository,"");
//
//        User user = User.builder()
//                .loginId(User.ANONYMOUS_LOGIN_ID)
//                .name(username)
//                .build();
//
//        UnnamedComment comment = UnnamedComment.builder()
//                .post(post)
//                .user(user)
//                .contents(contents)
//                .password(password)
//                .build();
//
//        unnamedCommentRepository.save(comment);
//    }
//
//    public void createNamedReply(Long userId, Long postId, Long commentId, Long taggedCommentId,String contents){
//        Post post = findByIdOrThrow(postId,postRepository,"");
//        AbstractComment superAbstractComment = findByIdOrThrow(commentId,commentRepository,"");
//        AbstractComment taggedAbstractComment = findByIdOrThrow(taggedCommentId,commentRepository,"");
//        User user = findByIdOrThrow(userId, userRepository,"");
//
//        AbstractComment reply = AbstractComment.builder()
//                .post(post)
//                .user(user)
//                .superComment(superAbstractComment)
//                .tag(taggedAbstractComment)
//                .contents(contents)
//                .build();
//
//        commentRepository.save(reply);
//    }
//
//
//    public void createUnnamedReply(Long postId, Long commentId,Long taggedCommentId, String contents,String username, String password){
//        Post post = findByIdOrThrow(postId,postRepository,"");
//
//        User user = User.builder()
//                .loginId(User.ANONYMOUS_LOGIN_ID)
//                .name(username)
//                .build();
//
//        AbstractComment superAbstractComment = findByIdOrThrow(commentId,commentRepository,"");
//        AbstractComment taggedAbstractComment = findByIdOrThrow(taggedCommentId,commentRepository,"");
//
//        UnnamedComment unnamedComment = UnnamedComment.builder()
//                .post(post)
//                .user(user)
//                .superComment(superAbstractComment)
//                .tag(taggedAbstractComment)
//                .contents(contents)
//                .password(password)
//                .build();
//
//        unnamedCommentRepository.save(unnamedComment);
//    }
//
//    // Retrieve -> Comment만
//    //TODO : void -> 변경필요
//    public List<CommentDTO.CommentListResponseDTO> retrieveCommentList(Long postId,Long userId){
//        //TODO : pagination 고려
//
//        User requestUser = findByIdOrThrow(userId, userRepository,"");
//
//        //TODO : JPQL 최적화 고려,
//        // 정렬 방법 -> superComment 생성 시간순으로 정렬 -> 자신의 생성 시간순으로 정렬
//        // 이렇게 하면 댓글 -> 답글 , 댓글 -> 답글 순으로 정렬 가능, 방법은 JPQL 만들고 결정해야할듯
//        List<AbstractComment> abstractComments = commentRepository.findByPostId(postId);
//        Collections.sort(abstractComments);
//
//        List<CommentDTO.CommentListResponseDTO> result = new ArrayList<>();
//
//        for (int i = 0; i < abstractComments.size();) {
//            AbstractComment superAbstractComment = abstractComments.get(i);
//            List<AbstractComment> replies = new ArrayList<>();
//            i++;
//            for (; i < abstractComments.size(); i++) {
//                AbstractComment reply = abstractComments.get(i);
//                if(reply.isReply()) replies.add(reply);
//                else break;
//            }
//
//            List<ReplyDTO.ReplyResponseDTO> repliesDTO = replies.stream().map(reply -> {
//                boolean isEditable = reply.isWrittenBy(requestUser);
//                return ReplyDTO.ReplyResponseDTO.toDTO(reply,isEditable);
//            }).collect(Collectors.toList());
//            boolean isEditable = superAbstractComment.isWrittenBy(requestUser);
//            CommentDTO.CommentListResponseDTO commentDTO = CommentDTO.CommentListResponseDTO.toDTO(superAbstractComment,isEditable,repliesDTO);
//            result.add(commentDTO);
//        }
//
//        return result;
//
//        /*
//        return comments.stream().map(comment -> {
//            boolean isEditable = comment.isWrittenBy(requestUser);
//            List<Comment> replies = commentRepository.findRepliesBySuperCommentId(comment.getSuperComment().getCommentId());
//            List<ReplyDTO.ReplyResponseDTO> replyResponseDTOS = replies.stream().map(reply -> {
//                boolean isEditableReply = reply.isWrittenBy(requestUser);
//                return ReplyDTO.ReplyResponseDTO.toDTO(reply, isEditableReply);
//            }).collect(Collectors.toList());
//            return CommentDTO.CommentListResponseDTO.toDTO(comment,isEditable,replyResponseDTOS);
//        }).collect(Collectors.toList());
//        */
//    }
//
//    public void changeContentsOfNamed(Long commentId, Long userId, String contents){
//        AbstractComment abstractComment = findByIdOrThrow(commentId,commentRepository,"");
//        User requestUser = findByIdOrThrow(userId, userRepository,"");
//
//        if(abstractComment.isNamed() && abstractComment.isWrittenBy(requestUser)) {
//            abstractComment.change(contents);
//            return;
//        }
//        //작성자가 다른 경우, 또는 익명 게시글이 선택된 경우
//        throw new IllegalArgumentException();
//    }
//
//    public void changeContentsOfUnnamed(Long commentId, String password, String contents){
//        UnnamedComment unnamedComment = findByIdOrThrow(commentId,unnamedCommentRepository,"");
//
//        if(unnamedComment.checkPassword(password)) {
//            unnamedComment.change(contents);
//            return;
//        }
//
//        // 패스워드가 다른 경우
//        throw new IllegalArgumentException();
//    }
//
//    public void deleteNamedComment(Long commentId, Long userId){
//        AbstractComment abstractComment = findByIdOrThrow(commentId,commentRepository,"");
//        User requestUser = findByIdOrThrow(userId, userRepository,"");
//
//        if (abstractComment.isWrittenBy(requestUser)) {
//            commentRepository.delete(abstractComment);
//            return;
//        }
//
//        // 작성자가 다른 경우
//        throw new IllegalArgumentException();
//    }
//
//    public void deleteUnnamedComment(Long commentId, String password){
//        UnnamedComment unnamedComment = findByIdOrThrow(commentId,unnamedCommentRepository,"");
//
//        if (unnamedComment.checkPassword(password)) {
//            unnamedCommentRepository.delete(unnamedComment);
//            return;
//        }
//
//        // 패스워드가 다른 경우
//        throw new IllegalArgumentException();
//    }
//
//    private <T> T findByIdOrThrow(Long id, JpaRepository<T, Long> repo, String errorMsg){
//        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(errorMsg));
//    }

}
