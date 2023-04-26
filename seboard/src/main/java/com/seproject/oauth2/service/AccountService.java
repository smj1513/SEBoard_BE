package com.seproject.oauth2.service;

import com.seproject.oauth2.controller.command.AccountRegisterCommand;
import com.seproject.oauth2.controller.command.OAuthAccountCommand;
import com.seproject.oauth2.controller.dto.AccountDTO;
import com.seproject.oauth2.repository.AccountRepository;
import com.seproject.oauth2.repository.RoleRepository;
import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.seproject.oauth2.controller.dto.AccountDTO.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isExist(String loginId){
        return accountRepository.existsByLoginId(loginId);
    }

    public void register(OAuthAccountCommand accountCommand) {
        Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> authorities = List.of(roleUser.get());

        Account account = Account.builder()
                .loginId(accountCommand.getId())
                .provider(accountCommand.getProvider())
                .username(accountCommand.getName())
                .nickname(accountCommand.getNickname())
                .password(UUID.randomUUID().toString())
                .email(accountCommand.getEmail())
                .profile(accountCommand.getProfile())
                .authorities(authorities)
                .build();

        accountRepository.save(account);
    }

    public Account findAccountById(String loginId) {
        return accountRepository.findByLoginId(loginId);
    }

    @Transactional
    public void register(AccountRegisterCommand accountRegisterCommand) {
        Optional<Role> roleUser = roleRepository.findByName("ROLE_KUMOH");
        List<Role> authorities = List.of(roleUser.get());

        Account account = Account.builder()
                .loginId(accountRegisterCommand.getId())
                .provider("se")
                .username(accountRegisterCommand.getName())
                .nickname(accountRegisterCommand.getNickname())
                .password(passwordEncoder.encode(accountRegisterCommand.getPassword()))
                .email(accountRegisterCommand.getId())
                .profile("none")
                .authorities(authorities)
                .build();

        accountRepository.save(account);
    }


    public RetrieveAllAccountResponse findAllAccount(int page,int perPage) {
        PageRequest pageRequest = PageRequest.of(page,perPage);
        Page<Account> all = accountRepository.findAll(pageRequest);
        List<Account> accounts = all.stream().collect(Collectors.toList());
        int total = all.getTotalPages();
        int nowPage = all.getNumber();

        return RetrieveAllAccountResponse.toDTO(total,nowPage+1,perPage,accounts);
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
                .email(request.getEmail())
                .profile(request.getProfile())
                .nickname(request.getNickname())
                .username(request.getName())
                .provider("se")
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
                .email(request.getEmail())
                .profile(request.getProfile())
                .nickname(request.getNickname())
                .username(request.getName())
                .authorities(convertAuthorities(request.getAuthorities()))
                .build();

        return UpdateAccountResponse.toDTO(account.update(updateAccount));
    }

}
