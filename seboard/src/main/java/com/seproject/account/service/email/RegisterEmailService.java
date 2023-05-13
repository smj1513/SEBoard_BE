package com.seproject.account.service.email;

import com.seproject.account.model.email.AccountRegisterConfirmedEmail;
import com.seproject.account.model.email.AccountRegisterEmail;
import com.seproject.account.repository.email.AccountRegisterConfirmedEmailRepository;
import com.seproject.account.repository.email.AccountRegisterEmailRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@EnableAsync
public class RegisterEmailService extends EmailService{
    private final AccountRegisterEmailRepository accountRegisterEmailRepository;
    private final AccountRegisterConfirmedEmailRepository accountRegisterConfirmedEmailRepository;

    @Async
    public void send(String email) {

        if(!isEmail(email)) {
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_MAIL,null);
        }

        AccountRegisterEmail accountRegisterEmail = new AccountRegisterEmail(email);

        Email simpleEmail = new SimpleEmail();
        simpleEmail.setHostName(host);
        simpleEmail.setSmtpPort(port);
        simpleEmail.setCharset("euc-kr"); // 인코딩 설정(utf-8, euc-kr)
        simpleEmail.setAuthenticator(new DefaultAuthenticator(username, password));
        simpleEmail.setTLS(true);

        try {
            simpleEmail.setFrom("jongjong159@naver.com");
            simpleEmail.setSubject("SE 게시판 회원가입 이메일 인증");
            simpleEmail.setMsg(accountRegisterEmail.toMessage());
            simpleEmail.addTo(email);

            accountRegisterEmailRepository.save(accountRegisterEmail);
            simpleEmail.send();
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void confirm(String email,String token) {
        Optional<AccountRegisterEmail> emailOptional = accountRegisterEmailRepository.findById(email);

        if(!isEmail(email)) {
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_MAIL,null);
        }

        if(emailOptional.isEmpty()) {
            throw new NoSuchElementException("일치하는 이메일 인증 정보가 없습니다.");
        }

        AccountRegisterEmail accountRegisterEmail = emailOptional.get();

        if(!accountRegisterEmail.confirm(token)) {
            throw new CustomIllegalArgumentException(ErrorCode.INCORRECT_AUTH_CODE,null);
        }

        AccountRegisterConfirmedEmail confirmedEmail = new AccountRegisterConfirmedEmail(email);
        accountRegisterConfirmedEmailRepository.save(confirmedEmail);
    }

    public boolean isConfirmed(String email) {

        if(!isEmail(email)) {
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_MAIL,null);
        }

        return accountRegisterConfirmedEmailRepository.findById(email).isPresent();
    }
}
