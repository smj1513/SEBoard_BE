package com.seproject.seboard.application;

import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.Anonymous;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.comment.ReplyRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.user.AnonymousRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.seproject.seboard.application.utils.AppServiceHelper.findByIdOrThrow;

@Service
@RequiredArgsConstructor
public class CommentAppService {
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AnonymousRepository anonymousRepository;

    public void writeNamedComment(Long accountId, Long postId, String contents) {
        Post post = findByIdOrThrow(postId, postRepository, "");
        Member member = memberRepository.findByAccountId(accountId);

        if (member == null) {
            //TODO : member 생성 로직 호출
        }

        //TODO : expose option 로직 추가
        Comment comment = post.writeComment(contents, member);

        commentRepository.save(comment);
    }

    @Transactional
    public void writeUnnamedComment(Long accountId, Long postId, String contents) {
        Post post = findByIdOrThrow(postId, postRepository, "");

        //TODO : JPQL로 변경?
        Anonymous author = getAnonymous(accountId, postId, post);

        //TODO : expose option 로직 추가
        Comment comment = post.writeComment(contents, author);

        commentRepository.save(comment);
    }


    public void writeNamedReply(Long accountId, Long postId, Long commentId, Long taggedCommentId, String contents) {
        Post post = findByIdOrThrow(postId, postRepository, "");

        Member member = memberRepository.findByAccountId(accountId);
        Comment superComment = findByIdOrThrow(commentId, commentRepository, "");
        Comment taggedComment = findByIdOrThrow(taggedCommentId, commentRepository, "");

        if (member == null) {
            //TODO : member 생성 로직 호출
        }

        //TODO : expose option 로직 추가
        Reply reply = superComment.writeReply(contents, taggedComment, member);

        commentRepository.save(reply);
    }


    @Transactional
    public void writeUnnamedReply(Long accountId, Long postId, Long commentId, Long taggedCommentId, String contents) {
        Post post = findByIdOrThrow(postId, postRepository, "");
        Comment superComment = findByIdOrThrow(commentId, commentRepository, "");
        Comment taggedComment = findByIdOrThrow(taggedCommentId, commentRepository, "");

        //TODO : JPQL로 변경?
        Anonymous author = getAnonymous(accountId, postId, post);

        //TODO : expose option 로직 추가
        Reply reply = superComment.writeReply(contents, taggedComment, author);

        replyRepository.save(reply);
    }

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

    public void editComment(String contents, Long commentId, Long accountId){
        Comment comment = findByIdOrThrow(commentId, commentRepository, "");

        if (!comment.isWrittenBy(accountId)) { //TODO : 관리자 권한의 경우 생각
            throw new IllegalArgumentException();
        }

        //TODO : exposeOption 고려
        comment.changeContents(contents);
    }

    public void removeComment(Long commentId, Long accountId) {
        Comment comment = findByIdOrThrow(commentId, commentRepository, "");

        if (!comment.isWrittenBy(accountId)) { //TODO : 관리자 권한의 경우 생각
            throw new IllegalArgumentException();
        }

        comment.delete();
    }

    public void removeReply(Long replyId, Long accountId) {
        Reply reply = findByIdOrThrow(replyId, replyRepository, "");

        if (!reply.isWrittenBy(accountId)) { //TODO : 관리자 권한의 경우 생각
            throw new IllegalArgumentException();
        }

        reply.delete();
    }

    private Anonymous getAnonymous(Long accountId, Long postId, Post post) {
        Anonymous author = commentRepository.findByPostId(postId).stream()
                .filter(comment -> comment.getAuthor().isAnonymous())
                .map(comment -> (Anonymous) comment.getAuthor())
                .filter(anonymous -> anonymous.isOwnAccountId(accountId))
                .findFirst()
                .orElseGet(() -> {
                    return replyRepository.findByPostId(postId).stream()
                            .filter(reply -> reply.getAuthor().isAnonymous())
                            .map(reply -> (Anonymous) reply.getAuthor())
                            .filter(anonymous -> anonymous.isOwnAccountId(accountId))
                            .findFirst()
                            .orElseGet(() -> {
                                Anonymous createdAnonymous = post.createAnonymous(accountId);
                                anonymousRepository.save(createdAnonymous);
                                return createdAnonymous;
                            });
                });
        return author;
    }

}
