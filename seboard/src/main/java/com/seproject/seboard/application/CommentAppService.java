package com.seproject.seboard.application;

import com.seproject.seboard.application.dto.comment.CommentCommand.CommentListFindCommand;
import com.seproject.seboard.controller.dto.PaginationResponse;
import com.seproject.seboard.controller.dto.comment.CommentResponse.CommentListElement;
import com.seproject.seboard.controller.dto.comment.CommentResponse.CommentListResponse;
import com.seproject.seboard.controller.dto.comment.ReplyResponse;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.Anonymous;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.Page;
import com.seproject.seboard.domain.repository.PagingInfo;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import com.seproject.seboard.domain.repository.comment.ReplyRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.user.AnonymousRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.seproject.seboard.application.utils.AppServiceHelper.findByIdOrThrow;

@Service
@RequiredArgsConstructor
public class CommentAppService {
    private final CommentRepository commentRepository;
    private final CommentSearchRepository commentSearchRepository;
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

    public CommentListResponse retrieveCommentList(CommentListFindCommand command) {
        Page<Comment> commentPage = commentSearchRepository.findCommentByPostId(command.getPostId(), new PagingInfo(command.getPage(), command.getPerPage()));

        List<CommentListElement> commentDtoList = commentPage.getData().stream()
                .map(comment -> {
                            List<ReplyResponse> subComments = commentSearchRepository.findReplyByCommentId(comment.getCommentId())
                                    .stream()
                                    .map(
                                            reply -> ReplyResponse.toDto(reply, reply.isWrittenBy(command.getAccountId()))
                                    ).collect(Collectors.toList());

                    return CommentListElement.toDto(comment, comment.isWrittenBy(command.getAccountId()), subComments);
                })
                .collect(Collectors.toList());

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .currentPage(commentPage.getCurPage())
                .contentSize(commentPage.getTotalSize())
                .perPage(commentPage.getPerPage())
                .lastPage(commentPage.getLastPage())
                .build();

        return CommentListResponse.toDto(commentDtoList, paginationResponse);
    }

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
