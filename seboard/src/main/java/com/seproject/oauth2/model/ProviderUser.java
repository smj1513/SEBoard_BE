package com.seproject.oauth2.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

public interface ProviderUser {

    String getId();
    String getUsername();
    String getPassword(); // 별로 의미가 없음 -> 랜덤값으로 만들기 때문에
    String getEmail();
    String getPicture();
    String getProvider();
    List<? extends GrantedAuthority> getAuthorities();

}
