package com.seproject.oauth2.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "email_authentication")
public class EmailAuthentication {

    @Id @GeneratedValue
    private Long id;

    private String email;

    @Column(name = "auth_token")
    private String authToken;
    private Boolean expired;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;


    private String generateAuthToken() {
        return UUID.randomUUID().toString().substring(0,8);
    }

    private LocalDateTime generateExpireDate() {
        return LocalDateTime.now().plusMinutes(10);
    }
    @Builder
    public EmailAuthentication(String email) {
        this.email = email;
        this.authToken = generateAuthToken();
        this.expired = false;
        this.expireDate = generateExpireDate();
    }

    public EmailAuthentication update() {
        authToken = generateAuthToken();
        expired = false;
        expireDate = generateExpireDate();

        return this;
    }

    public String getAuthToken(){
        return authToken;
    }

    public void setExpired(){
        this.expired = true;
    }

}
