package com.seproject.board.comment.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.account.domain.repository.AccountRepository;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomUserNotFoundException;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.board.comment.application.dto.CommentCommand.CommentEditCommand;
import com.seproject.board.comment.application.dto.CommentCommand.CommentListFindCommand;
import com.seproject.board.comment.application.dto.CommentCommand.CommentWriteCommand;
import com.seproject.board.comment.application.dto.ReplyCommand.ReplyEditCommand;
import com.seproject.board.comment.application.dto.ReplyCommand.ReplyWriteCommand;
import com.seproject.board.comment.controller.dto.PaginationResponse;
import com.seproject.board.comment.controller.dto.CommentResponse.CommentListElement;
import com.seproject.board.comment.controller.dto.CommentResponse.CommentListResponse;
import com.seproject.board.comment.controller.dto.ReplyResponse;
import com.seproject.board.comment.domain.model.Comment;
import com.seproject.board.comment.domain.model.Reply;
import com.seproject.board.post.domain.model.Post;
import com.seproject.board.post.domain.model.exposeOptions.ExposeState;
import com.seproject.member.domain.Anonymous;
import com.seproject.member.domain.Member;
import com.seproject.board.comment.domain.repository.CommentRepository;
import com.seproject.board.comment.domain.repository.CommentSearchRepository;
import com.seproject.board.comment.domain.repository.ReplyRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.member.domain.repository.AnonymousRepository;
import com.seproject.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        Account account = accountRepository.findByLoginId(command.getLoginId())
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

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

    public Long writeReply(ReplyWriteCommand command){
        Account account = accountRepository.findByLoginId(command.getLoginId())
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        if(command.isAnonymous()){
            return writeUnnamedReply(command, account.getAccountId());
        }else{
            return writeNamedReply(command, account.getAccountId());
        }
    }

    protected Long writeNamedReply(ReplyWriteCommand command, Long accountId){
        Post post = postRepository.findById(command.getPostId())
                .orElseThrow(()->new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        Member member = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER));

        Comment superComment = commentRepository.findById(command.getSuperCommentId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));
        Comment taggedComment = commentRepository.findById(command.getTagCommentId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));

        Reply reply = superComment.writeReply(command.getContents(), taggedComment, member, command.isOnlyReadByAuthor());

        commentRepository.save(reply);

        return reply.getCommentId();
    }


    protected Long writeUnnamedReply(ReplyWriteCommand command, Long accountId) {
        Post post = postRepository.findById(command.getPostId())
                .orElseThrow(()->new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        Comment superComment = commentRepository.findById(command.getSuperCommentId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));
        Comment taggedComment = commentRepository.findById(command.getTagCommentId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));

        Anonymous author = getAnonymous(accountId, command.getPostId(), post);

        Reply reply = superComment.writeReply(command.getContents(), taggedComment, author, command.isOnlyReadByAuthor());

        replyRepository.save(reply);

        return reply.getCommentId();
    }

    public CommentListResponse retrieveCommentList(CommentListFindCommand command) {
        //TODO : 리팩토링 필요
        Post post = postRepository.findById(command.getPostId()).orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

        //TODO : 비밀글일 때는?
        if(post.getExposeOption().getExposeState() == ExposeState.KUMOH){
            if(command.getLoginId()==null){
                throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
            }else{
                boolean isKumoh = SecurityUtils.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_KUMOH"));

                if(!isKumoh){
                    throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
                }
            }
        }

        //TODO : post의 접근 권한이 없을때 댓글 조회 안되야 함

        if(command.getLoginId()==null){
            Page<Comment> commentPage = commentSearchRepository.findCommentListByPostId(command.getPostId(), PageRequest.of(command.getPage(), command.getPerPage()));
            long totalReplySize = commentSearchRepository.countReplyByPostId(command.getPostId());

            List<CommentListElement> commentDtoList = commentPage.getContent().stream()
                    .map(comment -> {
                        List<ReplyResponse> subComments = commentSearchRepository.findReplyListByCommentId(comment.getCommentId())
                                .stream()
                                .map(
                                        reply -> ReplyResponse.toDto(reply, false, false)
                                ).collect(Collectors.toList());
                        return CommentListElement.toDto(comment, false,false, subComments);
                    }).collect(Collectors.toList());


            PaginationResponse paginationResponse = PaginationResponse.builder()
                    .totalCommentSize(commentPage.getTotalElements())
                    .last(commentPage.isLast())
                    .pageNum(commentPage.getNumber())
                    .totalAllSize(commentPage.getTotalElements() + totalReplySize)
                    .build();

            return CommentListResponse.toDto(commentDtoList, paginationResponse);


        }else{
            Account account = accountRepository.findByLoginId(command.getLoginId())
                    .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));


            Page<Comment> commentPage = commentSearchRepository.findCommentListByPostId(command.getPostId(), PageRequest.of(command.getPage(), command.getPerPage()));
            long totalReplySize = commentSearchRepository.countReplyByPostId(command.getPostId());

            List<CommentListElement> commentDtoList = commentPage.getContent().stream()
                    .map(comment -> {
                        List<ReplyResponse> subComments = commentSearchRepository.findReplyListByCommentId(comment.getCommentId())
                                .stream()
                                .map(
                                        reply -> ReplyResponse.toDto(reply, reply.isWrittenBy(account.getAccountId()) || post.getCategory().manageable(account.getAuthorities())
                                                , reply.getPost().isWrittenBy(account.getAccountId()))
                                ).collect(Collectors.toList());
                        return CommentListElement.toDto(
                                comment, comment.isWrittenBy(account.getAccountId()) || post.getCategory().manageable(account.getAuthorities())
                                ,comment.getPost().isWrittenBy(account.getAccountId()), subComments);
                    }).collect(Collectors.toList());


            PaginationResponse paginationResponse = PaginationResponse.builder()
                    .totalCommentSize(commentPage.getTotalElements())
                    .last(commentPage.isLast())
                    .pageNum(commentPage.getNumber())
                    .totalAllSize(commentPage.getTotalElements() + totalReplySize)
                    .build();

            return CommentListResponse.toDto(commentDtoList, paginationResponse);

        }
    }

    public Long editComment(CommentEditCommand command) {
        Account account = accountRepository.findByLoginId(command.getLoginId())
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        Comment comment = commentRepository.findById(command.getCommentId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));

        if (!comment.isWrittenBy(account.getAccountId()) && !comment.getPost().getCategory().manageable(account.getAuthorities())) {
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        comment.changeContents(command.getContents());
        comment.changeOnlyReadByAuthor(command.isOnlyReadByAuthor());

        return comment.getCommentId();
    }

    public Long editReply(ReplyEditCommand command) {
        Account account = accountRepository.findByLoginId(command.getLoginId())
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));
        Reply reply = replyRepository.findById(command.getReplyId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));

        if (!reply.isWrittenBy(account.getAccountId()) && !reply.getPost().getCategory().manageable(account.getAuthorities())) {
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        reply.changeContents(command.getContents());
        reply.changeOnlyReadByAuthor(command.isOnlyReadByAuthor());

        return reply.getCommentId();
    }

    public void removeComment(Long commentId, String loginId) {
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));

        if (!comment.isWrittenBy(account.getAccountId()) && !comment.getPost().getCategory().manageable(account.getAuthorities())) {
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        comment.delete(true);
    }

    public void removeReply(Long replyId, String loginId) {
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));

        if (!reply.isWrittenBy(account.getAccountId()) && !reply.getPost().getCategory().manageable(account.getAuthorities())) {
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        reply.delete(true);
    }

    protected Anonymous getAnonymous(Long accountId, Long postId, Post post) {
        //TODO : 리팩토링 필요
        Account account = accountRepository.findById(accountId).get();

        if(post.isWrittenBy(accountId) && !post.isNamed()){
            return anonymousRepository.findById(post.getAuthor().getBoardUserId()).get();
        }

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
                                Anonymous createdAnonymous = post.createAnonymous(account);
                                anonymousRepository.save(createdAnonymous);
                                return createdAnonymous;
                            });
                });
        return author;
    }

}