package com.seproject.account.service;

import com.seproject.account.model.social.OAuthAccount;
import com.seproject.account.repository.social.OAuthAccountRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.error.exception.CustomUserNotFoundException;
import com.seproject.account.repository.AccountRepository;
import com.seproject.account.repository.role.RoleRepository;
import com.seproject.account.model.Account;
import com.seproject.account.model.role.Role;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.seproject.admin.dto.AccountDTO.*;
import static com.seproject.account.controller.dto.RegisterDTO.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MemberRepository memberRepository;

    public boolean isExist(String loginId){
        return accountRepository.existsByLoginId(loginId);
    }
    public boolean isExistByNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }
    public boolean isOAuthUser(String loginId) {
        return oAuthAccountRepository.findByLoginId(loginId).isPresent();
    }


    private Role mapEmailToRole(String email) {

        if(emailService.isKumohMail(email)){
            return roleRepository.findByName("ROLE_KUMOH").orElseThrow();
        } else if(emailService.isEmail(email)) {
            return roleRepository.findByName("ROLE_USER").orElseThrow();
        }

        throw new CustomIllegalArgumentException(ErrorCode.INVALID_MAIL,null);
    }

    @Transactional
    public OAuthAccount register(OAuth2RegisterRequest oAuth2RegisterRequest) {
        String email = oAuth2RegisterRequest.getEmail();

        if(isExist(email)) throw new CustomIllegalArgumentException(ErrorCode.USER_ALREADY_EXIST, null);

        Role role = mapEmailToRole(email);
        List<Role> authorities = List.of(role);

        Account account = Account.builder()
                .loginId(email)
                .name(oAuth2RegisterRequest.getName())
                .nickname(oAuth2RegisterRequest.getNickname())
                .password(passwordEncoder.encode(oAuth2RegisterRequest.getPassword()))
                .authorities(authorities)
                .build();

        accountRepository.save(account);

        OAuthAccount oAuthAccount = OAuthAccount.builder()
                .sub(oAuth2RegisterRequest.getSubject())
                .provider(oAuth2RegisterRequest.getProvider())
                .account(account)
                .build();

        oAuthAccountRepository.save(oAuthAccount);

        Member member = Member.builder()
                .name(account.getNickname())
                .account(account)
                .build();

        memberRepository.save(member);

        return oAuthAccount;
    }

    @Transactional
    public Account register(FormRegisterRequest formRegisterRequest) {

        String id = formRegisterRequest.getId();
        if(isExist(id)) throw new CustomIllegalArgumentException(ErrorCode.USER_ALREADY_EXIST,null);
        Role role = mapEmailToRole(id);
        List<Role> authorities = List.of(role);

        Account account = Account.builder()
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


    public RetrieveAllAccountResponse findAllAccount(int page,int perPage) {

        try {
            PageRequest pageRequest = PageRequest.of(page,perPage);
            Page<Account> all = accountRepository.findAll(pageRequest);
            List<Account> accounts = all.stream().collect(Collectors.toList());
            int total = all.getTotalPages();
            int nowPage = all.getNumber();

            return RetrieveAllAccountResponse.toDTO(total,nowPage+1,perPage,accounts);
        } catch (IllegalArgumentException e) {
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_PAGINATION,e);
        }

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

    public CreateAccountResponse createAccount(CreateAccountRequest request) {

        Account account = Account.builder()
                .loginId(request.getId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .name(request.getName())
                .authorities(convertAuthorities(request.getAuthorities()))
                .build();

        Account savedAccount = accountRepository.save(account);
        return CreateAccountResponse.toDTO(savedAccount);
    }

    public UpdateAccountResponse updateAccount(UpdateAccountRequest request) {

        Account account = accountRepository.findById(request.getAccountId()).orElseThrow();

        Account updateAccount = Account.builder()
                .loginId(request.getId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .name(request.getName())
                .authorities(convertAuthorities(request.getAuthorities()))
                .build();

        return UpdateAccountResponse.toDTO(account.update(updateAccount));
    }

    public DeleteAccountResponse deleteAccount(Long accountId) {

        Account account = accountRepository.findById(accountId).orElseThrow();
        accountRepository.delete(account);

        return DeleteAccountResponse.toDTO(account);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginId(username);

        if(account == null) {
            throw new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null);
        }

        return account;
    }

}
