package com.seproject.oauth2.converters;

import com.seproject.oauth2.model.Attributes;
import com.seproject.oauth2.model.ProviderUser;
import com.seproject.oauth2.model.social.GoogleUser;
import com.seproject.oauth2.utils.OAuth2Utils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser>{

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        ClientRegistration clientRegistration = providerUserRequest.getClientRegistration();
        ProviderUser convertUser = null;
        OAuth2User oAuth2User = providerUserRequest.getOAuth2User();
        if(clientRegistration.getRegistrationId().equals("google")) {
            Attributes mainAttributes = OAuth2Utils.getMainAttributes(oAuth2User);
            convertUser = new GoogleUser(mainAttributes,oAuth2User, clientRegistration);
        }

        return convertUser;
    }
}
