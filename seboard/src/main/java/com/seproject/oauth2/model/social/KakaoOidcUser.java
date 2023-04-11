package com.seproject.oauth2.model.social;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.*;

public class KakaoOidcUser implements OidcUser

{
    private Map<String, Object> attributes;
    private Map<String, Object> claims;
    private OidcUserInfo oidcUserInfo;
    private OidcIdToken oidcIdToken;
    private List<? extends GrantedAuthority> authorities;

    public KakaoOidcUser(OidcUser oidcUser) {
        this.attributes = oidcUser.getAttributes();
        this.claims = oidcUser.getClaims();
        this.authorities = Collections.unmodifiableList(new ArrayList<>(oidcUser.getAuthorities()));
        this.oidcUserInfo = oidcUser.getUserInfo();
        this.oidcIdToken = oidcUser.getIdToken();
    }

    @Override
    public String getEmail() {
        return (String)getAttributes().get("email");
    }

    @Override
    public String getName() {
        return (String)getAttributes().get("nickname");
    }

    @Override
    public String getPicture() {
        return (String)getAttributes().get("picture");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUserInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcIdToken;
    }

    public String getProvider() {
        return "kakao";
    }

    public String getId() {return (String)getAttributes().get("sub");}
}
