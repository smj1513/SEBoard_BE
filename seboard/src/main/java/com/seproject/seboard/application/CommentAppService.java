package com.seproject.seboard.application;

import com.seproject.account.model.Account;
import com.seproject.account.repository.AccountRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentEditCommand;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentListFindCommand;
import com.seproject.seboard.application.dto.comment.CommentCommand.CommentWriteCommand;
import com.seproject.seboard.application.dto.comment.ReplyCommand.ReplyEditCommand;
import com.seproject.seboard.application.dto.comment.ReplyCommand.ReplyWriteCommand;
import com.seproject.seboard.controller.dto.PaginationResponse;
import com.seproject.seboard.controller.dto.comment.CommentResponse.CommentListElement;
import com.seproject.seboard.controller.dto.comment.CommentResponse.CommentListResponse;
import com.seproject.seboard.controller.dto.comment.ReplyResponse;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.Anonymous;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import com.seproject.seboard.domain.repository.comment.ReplyRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.user.AnonymousRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.seproject.seboard.application.utils.AppServiceHelper.findByIdOrThrow;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentAppService {
    private final CommentRepository commentRepository;
    private final CommentSearchRepository commentSearchRepository;
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AnonymousRepository anonymousRepository;
    private final AccountRepository accountRepository;

    public Long writeComment(CommentWriteCommand command){
        Account account = accountRepository.findByLoginId(command.getLoginId());

        if(command.isAnonymous()){
            return writeUnnamedComment(command, account.getAccountId());
        }else{
            return writeNamedComment(command, account.getAccountId());
        }
    }
    protected Long writeNamedComment(CommentWriteCommand command, Long accountId){
        Post post = postRepository.findById(command.getPostId())
                .orElseThrow(()->new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));
        Member member = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER));

        Comment comment = post.writeComment(command.getContents(), member, command.isOnlyReadByAuthor());

        commentRepository.save(comment);

        return comment.getCommentId();
    }


    protected Long writeUnnamedComment(CommentWriteCommand command, Long accountdId) {
        Post post = postRepository.findById(command.getPostId())
                .orElseThrow(()->new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        Anonymous author = getAnonymous(accountdId, command.getPostId(), post);

        Comment comment = post.writeComment(command.getContents(), author, command.isOnlyReadByAuthor());

        commentRepository.save(comment);

        return comment.getCommentId();
    }

    public void writeReply(ReplyWriteCommand command){
        if(command.isAnonymous()){
            writeUnnamedReply(command);
        }else{
            writeNamedReply(command);
        }
    }

    protected void writeNamedReply(ReplyWriteCommand command) {
        Post post = findByIdOrThrow(command.getPostId(), postRepository, "");

        Member member = memberRepository.findByAccountId(command.getAccountId()).orElseThrow(NoSuchElementException::new);
        Comment superComment = findByIdOrThrow(command.getSuperCommentId(), commentRepository, "");
        Comment taggedComment = findByIdOrThrow(command.getTagCommentId(), commentRepository, "");

        if (member == null) {
            //TODO : member 생성 로직 호출
        }

        Reply reply = superComment.writeReply(command.getContents(), taggedComment, member, command.isOnlyReadByAuthor());

        commentRepository.save(reply);
    }


    @Transactional
    protected void writeUnnamedReply(ReplyWriteCommand command) {
        Post post = findByIdOrThrow(command.getPostId(), postRepository, "");
        Comment superComment = findByIdOrThrow(command.getSuperCommentId(), commentRepository, "");
        Comment taggedComment = findByIdOrThrow(command.getTagCommentId(), commentRepository, "");

        //TODO : JPQL로 변경?
        Anonymous author = getAnonymous(command.getAccountId(), command.getPostId(), post);

        Reply reply = superComment.writeReply(command.getContents(), taggedComment, author, command.isOnlyReadByAuthor());

        replyRepository.save(reply);
    }

    public CommentListResponse retrieveCommentList(CommentListFindCommand command) {
        Page<Comment> commentPage = commentSearchRepository.findCommentListByPostId(command.getPostId(), PageRequest.of(command.getPage(), command.getPerPage()));
        long totalReplySize = commentSearchRepository.countReplyByPostId(command.getPostId());

        List<CommentListElement> commentDtoList = commentPage.getContent().stream()
                .map(comment -> {
                            List<ReplyResponse> subComments = commentSearchRepository.findReplyListByCommentId(comment.getCommentId())
                                    .stream()
                                    .map(
                                            reply -> ReplyResponse.toDto(reply, reply.isWrittenBy(command.getAccountId()))
                                    ).collect(Collectors.toList());
                    return CommentListElement.toDto(comment, comment.isWrittenBy(command.getAccountId()), subComments);
                }).collect(Collectors.toList());


        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalCommentSize(commentPage.getTotalElements())
                .last(commentPage.isLast())
                .pageNum(commentPage.getNumber())
                .totalAllSize(commentPage.getTotalElements() + totalReplySize)
                .build();

        return CommentListResponse.toDto(commentDtoList, paginationResponse);
    }

    public void editComment(CommentEditCommand command) {
        Comment comment = findByIdOrThrow(command.getCommentId(), commentRepository, "");

        //TODO : 추후 주석 해제 필요
        if (!comment.isWrittenBy(command.getAccountId())) { //TODO : 관리자 권한의 경우 생각
            throw new IllegalArgumentException();
        }

        comment.changeContents(command.getContents());
        comment.changeOnlyReadByAuthor(command.isOnlyReadByAuthor());
    }

    public void editReply(ReplyEditCommand command) {
        Reply reply = findByIdOrThrow(command.getReplyId(), replyRepository, "");

        //TODO : 추후 주석 해제 필요
        if (!reply.isWrittenBy(command.getAccountId())) { //TODO : 관리자 권한의 경우 생각
            throw new IllegalArgumentException();
        }

        reply.changeContents(command.getContents());
        reply.changeOnlyReadByAuthor(command.isOnlyReadByAuthor());
    }

    public void removeComment(Long commentId, Long accountId) {
        Comment comment = findByIdOrThrow(commentId, commentRepository, "");

        //TODO : 추후 주석 해제 필요
        if (!comment.isWrittenBy(accountId)) { //TODO : 관리자 권한의 경우 생각
            throw new IllegalArgumentException();
        }

        comment.delete();
    }

    public void removeReply(Long replyId, Long accountId) {
        Reply reply = findByIdOrThrow(replyId, replyRepository, "");

        //TODO : 추후 주석 해제 필요
        if (!reply.isWrittenBy(accountId)) { //TODO : 관리자 권한의 경우 생각
            throw new IllegalArgumentException();
        }

        reply.delete();
    }

    @Transactional
    protected Anonymous getAnonymous(Long accountId, Long postId, Post post) {
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
