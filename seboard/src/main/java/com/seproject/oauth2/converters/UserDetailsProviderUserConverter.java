package com.seproject.oauth2.converters;


import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.model.ProviderUser;
import com.seproject.oauth2.model.social.FormUser;

public class UserDetailsProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        Account account = providerUserRequest.getAccount();

        if(account == null) {
            return null;
        }

        return FormUser.builder()
                .id(account.getLoginId())
                .username(account.getUsername())
                .password(account.getPassword())
                .email(account.getEmail())
                .authorities(account.getAuthorities())
                .provider(account.getProvider())
                .build();




    }
}
