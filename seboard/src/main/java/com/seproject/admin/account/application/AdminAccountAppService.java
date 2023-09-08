package com.seproject.admin.account.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.account.persistence.AccountQueryRepository;
import com.seproject.account.account.service.AccountService;
import com.seproject.account.role.domain.Role;
import com.seproject.admin.account.controller.condition.AccountCondition;
import com.seproject.admin.account.controller.dto.AdminAccountDto.AccountResponse;
import com.seproject.admin.banned.service.BannedIdService;
import com.seproject.admin.banned.service.BannedNicknameService;
import com.seproject.account.role.service.RoleService;
import com.seproject.board.comment.domain.model.Comment;
import com.seproject.board.comment.domain.repository.CommentRepository;
import com.seproject.board.post.domain.model.Bookmark;
import com.seproject.board.post.domain.model.Post;
import com.seproject.board.post.domain.repository.BookmarkRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.seproject.admin.account.controller.dto.AdminAccountDto.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminAccountAppService {

    private final AccountService accountService;
    private final RoleService roleService;
    private final MemberService memberService;
    private final AccountQueryRepository accountQueryRepository;

    private final BannedIdService bannedIdService;
    private final BannedNicknameService bannedNicknameService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;


    public Page<AccountResponse> findAllAccount(AccountCondition condition, int page, int perPage) {
         //TODO :
        PageRequest pageRequest = PageRequest.of(page,perPage);
        Page<AccountResponse> response = accountQueryRepository.findAllAccount(condition, pageRequest);
        return response;
    }

    public AccountResponse findAccount(Long accountId) {
        Account account = accountService.findById(accountId);
        List<String> roles = account.getRoles()
                .stream().map(Role::toString)
                .collect(Collectors.toList());
        return AccountResponse.of(account,roles);
    }

    @Transactional
    public Long createAccount(CreateAccountRequest request) {

        String loginId = request.getId();
        if(accountService.isExistLoginId(loginId)){
            throw new CustomIllegalArgumentException(ErrorCode.USER_ALREADY_EXIST,null);
        }

        String nickname = request.getNickname();
        if (accountService.isExistNickname(nickname)) {
            throw new CustomIllegalArgumentException(ErrorCode.ALREADY_EXIST_NICKNAME,null);
        }

        if(!bannedIdService.possibleId(loginId)) {
            throw new CustomIllegalArgumentException(ErrorCode.BANNED_ID,null);
        }

        if(!bannedNicknameService.possibleNickname(nickname)) {
            throw new CustomIllegalArgumentException(ErrorCode.BANNED_NICKNAME,null);
        }

        List<Role> roles = roleService.findByNameIn(request.getRoles());

        Long accountId = accountService.createAccount(loginId, request.getPassword(), request.getName(), nickname, roles);
        Account account = accountService.findById(accountId);

        memberService.createMember(account);
        return accountId;
    }

    @Transactional
    public void updateAccount(Long accountId,UpdateAccountRequest request) {
        String loginId = request.getId();

        if (accountService.isExistLoginId(loginId)) {
            throw new CustomIllegalArgumentException(ErrorCode.USER_ALREADY_EXIST,null);
        }

        if(!bannedIdService.possibleId(loginId)) {
            throw new CustomIllegalArgumentException(ErrorCode.BANNED_ID,null);
        }

        String nickname = request.getNickname();
        if(!bannedNicknameService.possibleNickname(nickname)) {
            throw new CustomIllegalArgumentException(ErrorCode.BANNED_NICKNAME,null);
        }

        List<Role> roles = roleService.findByNameIn(request.getRoles());
        accountService.updateAccount(accountId, loginId,request.getPassword(),request.getName(), nickname,roles);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        accountService.deleteAccount(accountId);


        List<Post> writePost = postRepository.findByAccountId(accountId);

        for (Post post : writePost) {
            post.delete(true);
        }

        List<Comment> writeComment = commentRepository.findCommentsByAccountId(accountId);

        for (Comment comment : writeComment) {
            comment.delete(true);
        }

        List<Long> bookmarkIds = bookmarkRepository.findBookmarkByAccountId(accountId)
                .stream().map(Bookmark::getBookmarkId)
                .collect(Collectors.toList());
        bookmarkRepository.deleteAllByIdInBatch(bookmarkIds);
    }

    @Transactional
    public void deleteAccount(List<Long> accountIds) {

        //TODO : 벌크 삭제
        for (Long accountId : accountIds) {
            deleteAccount(accountId);
        }
    }

}
