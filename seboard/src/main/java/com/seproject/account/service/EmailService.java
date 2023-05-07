package com.seproject.account.service;

import com.seproject.account.model.EmailAuthentication;
import com.seproject.account.repository.EmailAuthRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@EnableAsync
public class EmailService {
    private final EmailAuthRepository emailAuthRepository;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    private static final String EMAIL_REGEX = "[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final String KUMOH_EMAIL_REGEX = "\\w+@kumoh.ac.kr";
    public boolean isEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public boolean isKumohMail(String email) {
        return email.matches(KUMOH_EMAIL_REGEX);
    }

    @Async
    public void send(String email) {
        EmailAuthentication emailAuthentication;

        if(!isEmail(email)) {
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_MAIL,null);
        }


        if(emailAuthRepository.existsByEmail(email)) {
            emailAuthentication = emailAuthRepository.findByEmail(email);
            emailAuthentication = emailAuthentication.update();
        } else {
            emailAuthentication = new EmailAuthentication(email);
        }

        Email simpleEmail = new SimpleEmail();
        simpleEmail.setHostName(host);
        simpleEmail.setSmtpPort(port);
        simpleEmail.setCharset("euc-kr"); // 인코딩 설정(utf-8, euc-kr)
        simpleEmail.setAuthenticator(new DefaultAuthenticator(username, password));
        simpleEmail.setTLS(true);

        try{
            simpleEmail.setFrom("jongjong159@naver.com");
            simpleEmail.setSubject("SE 게시판 회원가입 이메일 인증");
            simpleEmail.setMsg("인증번호: " + emailAuthentication.getAuthToken());
            simpleEmail.addTo(email);

            emailAuthRepository.save(emailAuthentication);
            simpleEmail.send();
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void confirm(String email,String token) {
        EmailAuthentication emailAuthentication = emailAuthRepository.findEmailAuthentication(email,token);

        if(!isEmail(email)) {
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_MAIL,null);
        }

        if(emailAuthentication == null) {
            throw new NoSuchElementException("일치하는 이메일 인증 정보가 없습니다.");
        }

        if(emailAuthentication.getExpireDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("만료된 인증 번호 입니다");
        }

        emailAuthentication.confirm();
    }

    public boolean isConfirmed(String email) {
        EmailAuthentication emailAuthentication = emailAuthRepository.findByEmail(email);

        if(!isEmail(email)) {
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_MAIL,null);
        }

        if(emailAuthentication == null) return false;

        if(emailAuthentication.getExpireDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        return emailAuthentication.getExpired();
    }
}
