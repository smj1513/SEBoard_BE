package com.seproject.oauth2.utils;

import com.seproject.oauth2.model.Attributes;
import com.seproject.oauth2.model.PrincipalUser;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class OAuth2Utils {

    public static Attributes getMainAttributes(OAuth2User oAuth2User) {
        return Attributes.builder()
                .mainAttributes(oAuth2User.getAttributes())
                .build();
    }

    public static Attributes getSubAttributes(OAuth2User oAuth2User, String subAttributesKey) {
        return Attributes.builder()
                .mainAttributes(oAuth2User.getAttributes())
                .subAttributes(oAuth2User.getAttribute(subAttributesKey))
                .build();
    }

    public static Attributes getOtherAttributes(OAuth2User oAuth2User, String subAttributesKey,String otherAttributesKey) {
//        oAuth2User.getAttributes().forEach((key,value) -> {
//            System.out.println(key + ":" + value);
//        });
        Map<String,Object> sub = oAuth2User.getAttribute(subAttributesKey);
        return Attributes.builder()
                .mainAttributes(oAuth2User.getAttributes())
                .subAttributes(sub)
                .otherAttributes((Map<String, Object>) sub.get(otherAttributesKey))
                .build();
    }

    public static String oAuth2UserName(OAuth2AuthenticationToken authentication, PrincipalUser principalUser) {
        String username;
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        OAuth2User oAuth2User = principalUser.getProviderUser().getOAuth2User();

        // Google
        Attributes attributes = OAuth2Utils.getMainAttributes(oAuth2User);
        username = (String) attributes.getMainAttributes().get("name");


        if(registrationId.equals("naver")) {
            attributes = OAuth2Utils.getSubAttributes(oAuth2User,"response");
            username = (String)attributes.getSubAttributes().get("name");
        } else if(registrationId.equals("kakao")) {

            //Oidc
            if(oAuth2User instanceof OidcUser) {
                attributes = OAuth2Utils.getMainAttributes(oAuth2User);
                username = (String) attributes.getMainAttributes().get("id");
            } else {
                attributes = OAuth2Utils.getOtherAttributes(principalUser,"kakao_account","profile");
                username = (String) attributes.getOtherAttributes().get("nickname");
            }


        }

        return username;
    }


}
