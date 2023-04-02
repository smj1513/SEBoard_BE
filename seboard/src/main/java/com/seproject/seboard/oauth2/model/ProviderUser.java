package com.seproject.seboard.oauth2.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface ProviderUser {

    String getId();
    String getUsername();
    String getPassword(); // 별로 의미가 없음 -> 랜덤값으로 만들기 때문에
    String getEmail();
    String getProvider();
    List<? extends GrantedAuthority> getAuthorities();
    Map<String, Object> getAttributes();
}
