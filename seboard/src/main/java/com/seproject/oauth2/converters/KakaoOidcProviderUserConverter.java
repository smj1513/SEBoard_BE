package com.seproject.oauth2.converters;

import com.seproject.oauth2.model.Attributes;
import com.seproject.oauth2.model.ProviderUser;
import com.seproject.oauth2.model.social.KakaoOidcUser;
import com.seproject.oauth2.utils.OAuth2Utils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KakaoOidcProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser>{


    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        ClientRegistration clientRegistration = providerUserRequest.getClientRegistration();
        OAuth2User oAuth2User = providerUserRequest.getOAuth2User();

        ProviderUser convertUser = null;
        if(clientRegistration.getRegistrationId().equals("kakao")) {
            if(oAuth2User instanceof OidcUser){
                Attributes attributes = OAuth2Utils.getMainAttributes(oAuth2User);
                convertUser = new KakaoOidcUser(attributes,oAuth2User,clientRegistration);
            }
        }

        return convertUser;
    }
}
