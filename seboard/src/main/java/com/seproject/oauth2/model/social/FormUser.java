package com.seproject.oauth2.model.social;

import com.seproject.oauth2.model.ProviderUser;
import com.seproject.oauth2.model.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class FormUser implements ProviderUser {

    private String id;
    private String username;
    private String password;
    private String email;
    private String provider;
    private List<Role> authorities;
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPicture() {
        return "none";
    }

    @Override
    public String getProvider() {
        return "none";
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
