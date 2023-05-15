package com.seproject.account.service.email;

import com.seproject.account.model.email.AccountRegisterConfirmedEmail;
import com.seproject.account.model.email.AccountRegisterEmail;
import com.seproject.account.repository.AccountRepository;
import com.seproject.account.repository.email.AccountRegisterConfirmedEmailRepository;
import com.seproject.account.repository.email.AccountRegisterEmailRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@EnableAsync
public class RegisterEmailService extends EmailService{
    private final AccountRegisterEmailRepository accountRegisterEmailRepository;
    private final AccountRegisterConfirmedEmailRepository accountRegisterConfirmedEmailRepository;
    private final AccountRepository accountRepository;

    @Async
    @Override
    protected void send(String email,SimpleEmail simpleEmail) throws EmailException {
        AccountRegisterEmail accountRegisterEmail = new AccountRegisterEmail(email);

        if(accountRepository.existsByLoginId(email)) {
            throw new CustomIllegalArgumentException(ErrorCode.USER_ALREADY_EXIST,null);
        }

        simpleEmail.setSubject("SE 게시판 회원가입 이메일 인증");
        simpleEmail.setMsg(accountRegisterEmail.toMessage());
        simpleEmail.addTo(email);

        accountRegisterEmailRepository.save(accountRegisterEmail);
        simpleEmail.send();
    }

    @Override
    public void confirmEmail(String email,String token) {
        Optional<AccountRegisterEmail> emailOptional = accountRegisterEmailRepository.findById(email);

        if(emailOptional.isEmpty()) {
            throw new CustomIllegalArgumentException(ErrorCode.EMAIL_NOT_FOUNT,null);
        }

        AccountRegisterEmail accountRegisterEmail = emailOptional.get();

        if(!accountRegisterEmail.confirm(token)) {
            throw new CustomIllegalArgumentException(ErrorCode.INCORRECT_AUTH_CODE,null);
        }

        AccountRegisterConfirmedEmail confirmedEmail = new AccountRegisterConfirmedEmail(email);
        accountRegisterConfirmedEmailRepository.save(confirmedEmail);
    }

    @Override
    public boolean isConfirmedEmail(String email) {
        return accountRegisterConfirmedEmailRepository.existsById(email);
    }
}
