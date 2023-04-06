package com.seproject.oauth2.model.social;

import com.seproject.oauth2.model.Attributes;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoUser extends OAuth2ProviderUser
{
    private Map<String,Object> accountAttributes;
    private Map<String,Object> profileAttributes;
    public KakaoUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration registration) {
        super(attributes.getMainAttributes(),oAuth2User, registration);
        accountAttributes = attributes.getSubAttributes();
        profileAttributes = attributes.getOtherAttributes();
    }


    @Override
    public String getEmail() {
        return (String)accountAttributes.get("email");
    }

    @Override
    public String getId() {
        return String.valueOf(getAttributes().get("id"));
    }

    @Override
    public String getUsername() {
        return (String)profileAttributes.get("nickname"); //email?
    }

    @Override
    public String getPicture() {

        if(!Boolean.getBoolean((String)accountAttributes.get("is_default_image"))) {
            return (String)profileAttributes.get("profile_image_url");
        }
        return "none";
    }
}
