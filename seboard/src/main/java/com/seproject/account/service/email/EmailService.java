package com.seproject.account.service.email;

import com.seproject.account.model.email.AccountRegisterConfirmedEmail;
import com.seproject.account.model.email.AccountRegisterEmail;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class EmailService {

    @Value("${mail.host}")
    protected String host;

    @Value("${mail.port}")
    protected int port;

    @Value("${mail.username}")
    protected String username;

    @Value("${mail.password}")
    protected String password;

    protected static final String EMAIL_REGEX = "[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    protected static final String KUMOH_EMAIL_REGEX = "\\w+@kumoh.ac.kr";
    public boolean isEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public boolean isKumohMail(String email) {
        return email.matches(KUMOH_EMAIL_REGEX);
    }

    public abstract void send(String email) ;
    public abstract void confirm(String email,String token) ;

    public abstract boolean isConfirmed(String email);

}
