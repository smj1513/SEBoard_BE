package com.seproject.oauth2.service;

import com.seproject.oauth2.converters.DelegationProviderUserConverter;
import com.seproject.oauth2.converters.ProviderUserRequest;
import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.model.PrincipalUser;
import com.seproject.oauth2.model.ProviderUser;
import com.seproject.oauth2.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuth2LoginService extends AbstractOAuth2LoginService implements OAuth2UserService<OAuth2UserRequest,OAuth2User> {

    public OAuth2LoginService(AccountRepository accountRepository,
                              AccountService accountService,
                              DelegationProviderUserConverter providerUserConverter) {
        super(accountRepository, accountService, providerUserConverter);
    }

    public void registerUser(ProviderUser oAuth2User,String nickname) {
        Account account = accountRepository.findByLoginId(oAuth2User.getProvider() + "_" + oAuth2User.getId());
        if(account != null) throw new IllegalArgumentException("이미 회원가입된 사용자입니다.");
        accountService.registerWithNickname(oAuth2User.getProvider(),oAuth2User,nickname);
//        accountService.register(oAuth2User.getProvider(),oAuth2User);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //사용자의 로그인 요청에서 레지스트레이션 가져옴
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        //기본 OAuth2Service 이용하여 사용자 요청을 이용하여 인가 서버와 통신 준비
        OAuth2UserService<OAuth2UserRequest,OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // 인가 서버와 통신하여 사용자 정보를 조회 -> OAuth2User 타입 객체에 사용자 정보를 저장
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration,oAuth2User);

        // 조회한 사용자 정보와 레지스트레이션 정보를 이용하여 provider에 맞는 account 객체로 생성
        ProviderUser OAuth2ProviderUser = super.providerUser(providerUserRequest);

//        //회원가입 로직
//        super.register(OAuth2ProviderUser,userRequest);

        return new PrincipalUser(OAuth2ProviderUser);
    }
}
