package com.seproject.account.service;

import com.seproject.account.model.account.FormAccount;
import com.seproject.account.model.account.OAuthAccount;
import com.seproject.account.repository.social.OAuthAccountRepository;
import com.seproject.account.service.email.KumohEmailService;
import com.seproject.account.service.email.RegisterEmailService;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.admin.domain.repository.AdminAccountSearchRepository;
import com.seproject.admin.service.BannedIdService;
import com.seproject.admin.service.BannedNicknameService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.error.exception.CustomUserNotFoundException;
import com.seproject.account.repository.AccountRepository;
import com.seproject.account.repository.role.RoleRepository;
import com.seproject.account.model.account.Account;
import com.seproject.account.model.role.Role;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.post.Bookmark;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.BoardUser;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.post.BookmarkRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import com.seproject.seboard.domain.repository.user.BoardUserRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.seproject.admin.dto.AccountDTO.*;
import static com.seproject.account.controller.dto.AccountDTO.*;
import static com.seproject.account.controller.dto.RegisterDTO.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegisterEmailService registerEmailService;
    private final MemberRepository memberRepository;
    private final AdminAccountSearchRepository accountSearchRepository;

    private final BannedIdService bannedIdService;
    private final BannedNicknameService bannedNicknameService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final KumohEmailService kumohEmailService;

    public boolean isExist(String loginId){
        return accountRepository.existsByLoginId(loginId);
    }
    public boolean isExistByNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }
    public boolean isOAuthUser(String loginId) {
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));
        return account.getClass() == OAuthAccount.class;
    }

    private Role mapEmailToRole(String email) {

        if(registerEmailService.isKumohMail(email)){
            return roleRepository.findByName("ROLE_KUMOH").orElseThrow();
        } else if(registerEmailService.isEmail(email)) {
            return roleRepository.findByName("ROLE_USER").orElseThrow();
        }

        throw new CustomIllegalArgumentException(ErrorCode.INVALID_MAIL,null);
    }
    @Transactional
    public OAuthAccount register(OAuth2RegisterRequest oAuth2RegisterRequest) {
        String email = oAuth2RegisterRequest.getEmail();

        if(isExist(email)) throw new CustomIllegalArgumentException(ErrorCode.USER_ALREADY_EXIST, null);
        if(!bannedIdService.possibleId(email)) throw new CustomIllegalArgumentException(ErrorCode.BANNED_ID,null);
        if(!bannedNicknameService.possibleNickname(oAuth2RegisterRequest.getNickname())) throw new CustomIllegalArgumentException(ErrorCode.BANNED_NICKNAME,null);

        Role role = mapEmailToRole(email);
        List<Role> authorities = List.of(role);

        OAuthAccount oAuthAccount = OAuthAccount.builder()
                .loginId(email)
                .name(oAuth2RegisterRequest.getName())
                .nickname(oAuth2RegisterRequest.getNickname())
                .password(passwordEncoder.encode(oAuth2RegisterRequest.getPassword()))
                .authorities(authorities)
                .sub(oAuth2RegisterRequest.getSubject())
                .provider(oAuth2RegisterRequest.getProvider())
                .build();

        oAuthAccountRepository.save(oAuthAccount);

        Member member = Member.builder()
                .name(oAuthAccount.getNickname())
                .account(oAuthAccount)
                .build();

        memberRepository.save(member);

        return oAuthAccount;
    }
    @Transactional
    public Account register(FormRegisterRequest formRegisterRequest) {

        String id = formRegisterRequest.getId();

        if(isExist(id)) throw new CustomIllegalArgumentException(ErrorCode.USER_ALREADY_EXIST,null);
        if(!bannedIdService.possibleId(id)) throw new CustomIllegalArgumentException(ErrorCode.BANNED_ID,null);
        if(!bannedNicknameService.possibleNickname(formRegisterRequest.getNickname())) throw new CustomIllegalArgumentException(ErrorCode.BANNED_NICKNAME,null);

        Role role = mapEmailToRole(id);
        List<Role> authorities = List.of(role);

        FormAccount account = FormAccount.builder()
                .loginId(id)
                .name(formRegisterRequest.getName())
                .nickname(formRegisterRequest.getNickname())
                .password(passwordEncoder.encode(formRegisterRequest.getPassword()))
                .authorities(authorities)
                .build();

        accountRepository.save(account);

        Member member = Member.builder()
                .name(account.getNickname())
                .account(account)
                .build();

        memberRepository.save(member);
        return account;
    }
    public Page<RetrieveAccountResponse> findAllAccount(AdminRetrieveAccountCondition condition, Pageable pageable) {
        return accountSearchRepository.findAllAccount(condition, pageable);
    }
    public RetrieveAccountResponse findAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return RetrieveAccountResponse.toDTO(account);
    }
    private List<Role> convertAuthorities(List<String> authorities) {
        List<Role> convertedAuthorities = roleRepository.findByNameIn(authorities);

        if(authorities.size() != convertedAuthorities.size()) {
            throw new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null);
        }

        return convertedAuthorities;
    }
    @Transactional
    public CreateAccountResponse createAccount(CreateAccountRequest request) {

        String id = request.getId();
        String nickname = request.getNickname();
        if(isExist(id))  throw new CustomIllegalArgumentException(ErrorCode.USER_ALREADY_EXIST,null);
        if(!bannedIdService.possibleId(id)) throw new CustomIllegalArgumentException(ErrorCode.BANNED_ID,null);
        if(!bannedNicknameService.possibleNickname(nickname)) throw new CustomIllegalArgumentException(ErrorCode.BANNED_NICKNAME,null);

        FormAccount account = FormAccount.builder()
                .loginId(id)
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(nickname)
                .name(request.getName())
                .authorities(convertAuthorities(request.getAuthorities()))
                .build();

        Account savedAccount = accountRepository.save(account);

        Member member = Member.builder()
                .name(account.getNickname())
                .account(account)
                .build();

        memberRepository.save(member);

        return CreateAccountResponse.toDTO(savedAccount);
    }
    @Transactional
    public UpdateAccountResponse updateAccount(UpdateAccountRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.USER_NOT_FOUND,null));

        String id = request.getId();
        String nickname = request.getNickname();

        //TODO : 자기 자신 빼고 이미 존재하는지 확인
        if(!bannedIdService.possibleId(id)) throw new CustomIllegalArgumentException(ErrorCode.BANNED_ID,null);
        if(!bannedNicknameService.possibleNickname(nickname)) throw new CustomIllegalArgumentException(ErrorCode.BANNED_NICKNAME,null);

        Account update = account.update(id,
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                nickname,
                convertAuthorities(request.getAuthorities()));

        return UpdateAccountResponse.toDTO(update);
    }

    @Transactional
    public DeleteAccountResponse deleteAccount(Long accountId) {

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        if(isOAuthUser(account.getLoginId())) {
            OAuthAccount oAuthAccount = oAuthAccountRepository.findByLoginId(account.getLoginId())
                    .orElseThrow(() -> new RuntimeException("oAuth 유저 체크는 통과했으나 찾을수 없는 경우"));
            oAuthAccount.removeSub();
//            TODO : 외래키 제약조건 걸림 oAuthAccountRepository.delete(oAuthAccount);
        }

        account.delete(true);

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

        return DeleteAccountResponse.toDTO(account);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginId(username)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));
        return account;
    }

    @Transactional
    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        String newPassword = resetPasswordRequest.getPassword();
        Account account = accountRepository.findByLoginId(email)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        if(account == null) {
            throw new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null);
        }

        account.changePassword(passwordEncoder.encode(newPassword));

        return ResetPasswordResponse.toDTO(account);
    }

    @Transactional
    public void changePassword(PasswordChangeRequest request) {
        String loginId = SecurityUtils.getLoginId();
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        if(!passwordEncoder.matches(request.getNowPassword(),account.getPassword())){
            throw new CustomIllegalArgumentException(ErrorCode.PASSWORD_INCORRECT,null);
        }

        account.changePassword(passwordEncoder.encode(request.getNewPassword()));

    }

    @Transactional
    public KumohAuthResponse grantKumohAuth(KumohAuthRequest kumohAuthRequest) {
        String email = kumohAuthRequest.getEmail();
        String loginId = SecurityUtils.getLoginId();

        if(loginId == null)
            throw new CustomAuthenticationException(ErrorCode.NOT_LOGIN,null);
        if(!kumohEmailService.isConfirmed(email))
            throw new CustomIllegalArgumentException(ErrorCode.EMAIL_NOT_FOUNT,null);

        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        List<Role> authorities = account.getAuthorities();

        boolean flag = false;
        for (Role authority : authorities) {
            flag |= authority.getAuthority().equals(Role.ROLE_KUMOH);
        }

        if(!flag) {
            Role kumoh = roleRepository.findByName(Role.ROLE_KUMOH).get();
            authorities.add(kumoh);
        }

        return KumohAuthResponse.toDTO(account);
    }

    public MyInfoResponse findMyInfo(String loginId) {
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

        return MyInfoResponse.toDTO(account.getLoginId(),account.getNickname(),account.getAuthorities().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList()));
    }

    @Transactional
    public Account changeNickname(String loginId,String nickname) {
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND, null));

        account.changeNickname(nickname);

        return account;
    }

}
