package com.seproject.admin.account.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.account.persistence.AccountQueryRepository;
import com.seproject.account.account.service.AccountService;
import com.seproject.account.role.domain.Role;
import com.seproject.account.role.service.RoleService;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.admin.account.controller.condition.AccountCondition;
import com.seproject.admin.account.controller.dto.AdminAccountDto.AccountResponse;
import com.seproject.admin.banned.service.BannedIdService;
import com.seproject.admin.banned.service.BannedNicknameService;
import com.seproject.admin.dashboard.domain.DashBoardMenu;
import com.seproject.admin.dashboard.service.AdminDashBoardServiceImpl;
import com.seproject.admin.role.controller.dto.RoleDTO;
import com.seproject.admin.role.persistence.RoleQueryRepository;
import com.seproject.board.comment.domain.model.Comment;
import com.seproject.board.comment.domain.repository.CommentRepository;
import com.seproject.board.common.Status;
import com.seproject.board.post.domain.model.Bookmark;
import com.seproject.board.post.domain.model.Post;
import com.seproject.board.post.domain.repository.BookmarkRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAccessDeniedException;
import com.seproject.error.exception.CustomAuthenticationException;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.member.domain.Member;
import com.seproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.seproject.admin.account.controller.dto.AdminAccountDto.CreateAccountRequest;
import static com.seproject.admin.account.controller.dto.AdminAccountDto.UpdateAccountRequest;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminAccountAppService {

    private final AccountService accountService;
    private final RoleService roleService;
    private final MemberService memberService;
    private final AccountQueryRepository accountQueryRepository;
    private final RoleQueryRepository roleQueryRepository;
    private final BannedIdService bannedIdService;
    private final BannedNicknameService bannedNicknameService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;

    private final AdminDashBoardServiceImpl dashBoardService;

    //TODO : 추후 AOP로 변경 필요
    private void checkAuthorization(String url){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new CustomAuthenticationException(ErrorCode.NOT_LOGIN, null));

        DashBoardMenu dashboardmenu = dashBoardService.findDashBoardMenuByUrl(url);

        if(!dashboardmenu.authorize(account.getRoles())){
            throw new CustomAccessDeniedException(ErrorCode.ACCESS_DENIED, null);
        }
    }


    public Page<AccountResponse> findAllAccount(AccountCondition condition, int page, int perPage) {
        if(condition.getStatus()== Status.TEMP_DELETED){
            checkAuthorization(DashBoardMenu.TRASH_URL);
        }else{
            checkAuthorization(DashBoardMenu.ACCOUNT_MANAGE_URL);
        }

        PageRequest pageRequest = PageRequest.of(page,perPage);
        Page<AccountResponse> response = accountQueryRepository.findAllAccount(condition, pageRequest);

        List<Long> ids = response.getContent()
                .stream().map(AccountResponse::getAccountId)
                .collect(Collectors.toList());

        Map<Long, List<RoleDTO.RoleResponse>> accountRole = roleQueryRepository.findAccountRole(ids);

        List<AccountResponse> content = response.getContent();
        for (AccountResponse accountResponse : content) {
            accountResponse.setRoles(accountRole.get(accountResponse.getAccountId()));
        }

        return response;
    }

    public AccountResponse findAccount(Long accountId) {
        checkAuthorization(DashBoardMenu.ACCOUNT_MANAGE_URL);

        Account account = accountService.findById(accountId);
        List<RoleDTO.RoleResponse> roles = account.getRoles()
                .stream().map(RoleDTO.RoleResponse :: of)
                .collect(Collectors.toList());
        return AccountResponse.of(account,roles);
    }

    @Transactional
    public Long createAccount(CreateAccountRequest request) {
        checkAuthorization(DashBoardMenu.ACCOUNT_MANAGE_URL);

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

        List<Role> roles = roleService.findByIds(request.getRoles());

        Long accountId = accountService.createAccount(loginId, request.getPassword(), request.getName(), roles);
        Account account = accountService.findById(accountId);

        memberService.createMember(account,nickname);
        return accountId;
    }

    @Transactional
    public void updateAccount(Long accountId,UpdateAccountRequest request) {
        checkAuthorization(DashBoardMenu.ACCOUNT_MANAGE_URL);

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

        Member member = memberService.findByAccountId(accountId);
        member.changeName(nickname);

        List<Role> roles = roleService.findByIds(request.getRoles());
        accountService.updateAccount(accountId, loginId,request.getPassword(),request.getName(),roles);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        checkAuthorization(DashBoardMenu.ACCOUNT_MANAGE_URL);

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
        checkAuthorization(DashBoardMenu.ACCOUNT_MANAGE_URL);

        //TODO : 벌크 삭제
        for (Long accountId : accountIds) {
            deleteAccount(accountId);
        }
    }

}
