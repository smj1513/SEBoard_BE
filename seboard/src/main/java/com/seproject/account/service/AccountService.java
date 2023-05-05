package com.seproject.account.service;

import com.seproject.account.model.social.OAuthAccount;
import com.seproject.account.repository.OAuthAccountRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidPaginationException;
import com.seproject.account.repository.AccountRepository;
import com.seproject.account.repository.RoleRepository;
import com.seproject.account.model.Account;
import com.seproject.account.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class AccountService {

    private final AccountRepository accountRepository;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public boolean isExist(String loginId){
        return accountRepository.existsByLoginId(loginId);
    }
    public boolean isExistByNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }
    public boolean isOAuthUser(String subject) {
        return oAuthAccountRepository.existsBySub(subject);
    }
    @Transactional
    public OAuthAccount register(OAuth2RegisterRequest oAuth2RegisterRequest) {
        String email = oAuth2RegisterRequest.getEmail();

        if(isExist(email)) throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        Role role;

        if(emailService.isKumohMail(email)){
            role = roleRepository.findByName("ROLE_KUMOH").orElseThrow();
        } else if(emailService.isEmail(email)) {
            role = roleRepository.findByName("ROLE_USER").orElseThrow();
        } else {
            throw new IllegalArgumentException("잘못된 이메일");
        }

        List<Role> authorities = List.of(role);

        Account account = Account.builder()
                .loginId(email)
                .username(oAuth2RegisterRequest.getName())
                .nickname(oAuth2RegisterRequest.getNickname())
                .password(passwordEncoder.encode(oAuth2RegisterRequest.getPassword()))
                .authorities(authorities)
                .build();

        OAuthAccount oAuthAccount = OAuthAccount.builder()
                .sub(oAuth2RegisterRequest.getSubject())
                .provider(oAuth2RegisterRequest.getProvider())
                .account(account)
                .build();

        accountRepository.save(account);
        oAuthAccountRepository.save(oAuthAccount);

        return oAuthAccount;
    }

    @Transactional
    public Account register(FormRegisterRequest formRegisterRequest) {

        String id = formRegisterRequest.getId();
        Role role;

        if(emailService.isKumohMail(id)){
            role = roleRepository.findByName("ROLE_KUMOH").orElseThrow();
        } else if(emailService.isEmail(id)) {
            role = roleRepository.findByName("ROLE_USER").orElseThrow();
        } else {
            throw new IllegalArgumentException("잘못된 이메일");
        }

        List<Role> authorities = List.of(role);

        Account account = Account.builder()
                .loginId(id)
                .username(formRegisterRequest.getName())
                .nickname(formRegisterRequest.getNickname())
                .password(passwordEncoder.encode(formRegisterRequest.getPassword()))
                .authorities(authorities)
                .build();

        accountRepository.save(account);

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
            throw new InvalidPaginationException(ErrorCode.INVALID_PAGINATION);
        }

    }

    public RetrieveAccountResponse findAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return RetrieveAccountResponse.toDTO(account);
    }

    private List<Role> convertAuthorities(List<String> authorities) {
        List<Role> convertedAuthorities = roleRepository.findByNameIn(authorities);

        if(authorities.size() != convertedAuthorities.size()) {
            throw new IllegalArgumentException("존재하지 않는 권한을 요청하였습니다.");
        }

        return convertedAuthorities;
    }

    public CreateAccountResponse createAccount(CreateAccountRequest request) {

        Account account = Account.builder()
                .loginId(request.getId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .username(request.getName())
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
                .username(request.getName())
                .authorities(convertAuthorities(request.getAuthorities()))
                .build();

        return UpdateAccountResponse.toDTO(account.update(updateAccount));
    }

    public DeleteAccountResponse deleteAccount(Long accountId) {

        Account account = accountRepository.findById(accountId).orElseThrow();
        accountRepository.delete(account);

        return DeleteAccountResponse.toDTO(account);
    }

}
