package com.seproject.seboard.oauth2.converters;

import com.seproject.seboard.oauth2.model.Attributes;
import com.seproject.seboard.oauth2.model.social.NaverUser;
import com.seproject.seboard.oauth2.model.ProviderUser;
import com.seproject.seboard.oauth2.utils.OAuth2Utils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser>{


    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        ClientRegistration clientRegistration = providerUserRequest.getClientRegistration();
        OAuth2User oAuth2User = providerUserRequest.getOAuth2User();

        ProviderUser convertUser = null;
        if(clientRegistration.getRegistrationId().equals("naver")) {
            Attributes attributes = OAuth2Utils.getSubAttributes(oAuth2User,"response");
            convertUser = new NaverUser(attributes,oAuth2User,clientRegistration);
        }

        return convertUser;
    }
}
