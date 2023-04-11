package com.seproject.oauth2.model.social;

import com.seproject.oauth2.model.ProviderUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class OAuth2ProviderUser implements ProviderUser,OAuth2User {

    private OAuth2User oAuth2User;
    private ClientRegistration registration;
    private Map<String,Object> attributes;

    public OAuth2ProviderUser(OAuth2User oAuth2User, ClientRegistration registration) {
        this.oAuth2User = oAuth2User;
        this.registration = registration;
        this.attributes = oAuth2User.getAttributes();
    }

    public OAuth2ProviderUser(Map<String,Object> attributes, OAuth2User oAuth2User, ClientRegistration registration) {
        this.oAuth2User = oAuth2User;
        this.registration = registration;
        this.attributes = attributes;
    }

    @Override
    public String getPassword() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getProvider() {
        return registration.getRegistrationId();
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
    }


    public Map<String, Object> getAttributes() {
        return attributes;
    }


    public OAuth2User getOAuth2User() {
        return oAuth2User;
    }
}
