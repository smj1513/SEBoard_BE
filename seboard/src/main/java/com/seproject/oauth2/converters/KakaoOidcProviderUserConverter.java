//package com.seproject.oauth2.converters;
//
//import com.seproject.oauth2.model.ProviderUser;
//import com.seproject.oauth2.model.social.KakaoOidcUser;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//public class KakaoOidcProviderUserConverter implements ProviderUserConverter<OidcUserRequest, ProviderUser>{
//
//
//    @Override
//    public ProviderUser convert(OidcUserRequest oidcUserRequest) {
//        ClientRegistration clientRegistration = oidcUserRequest.getClientRegistration();
//        OAuth2User oAuth2User = providerUserRequest.getOAuth2User();
//
//        ProviderUser convertUser = null;
//        if(clientRegistration.getRegistrationId().equals("kakao")) {
//            if(oAuth2User instanceof OidcUser){
//                convertUser = new KakaoOidcUser(oAuth2User,clientRegistration);
//
//                oAuth2User.getAttributes().forEach((key,value) -> {
//                    System.out.println(key + ":" + value);
//                });
//            }
//        }
//
//        return convertUser;
//    }
//}
