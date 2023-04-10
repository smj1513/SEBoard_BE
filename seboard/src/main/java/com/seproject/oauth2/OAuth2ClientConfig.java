package com.seproject.oauth2;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.seproject.oauth2.model.PrincipalUser;
import com.seproject.oauth2.model.ProviderUser;
import com.seproject.oauth2.service.OAuth2LoginService;
import com.seproject.oauth2.service.CustomOidcUserService;
import com.seproject.oauth2.utils.CustomAuthorityMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@AllArgsConstructor
@EnableWebSecurity
public class OAuth2ClientConfig {

    private ClientRegistrationRepository clientRegistrationRepository;
    private final CustomOidcUserService customOidcUserService;
    private final OAuth2LoginService customOAuth2UserService;
    private static final String SECRET = "=gfasdkhfadskljhfadsiulhfdsaliuhliu"; // 우리 서버만 알고 있는 비밀값
    private static final int EXPIRATION_TIME = 864000000; // 10일 (1/1000초)
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring().antMatchers("/static/**").antMatchers("/error");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll();

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//        http.formLogin().loginPage("/login").loginProcessingUrl("/loginProc").permitAll();
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfoEndpointConfig ->
                userInfoEndpointConfig.userService(customOAuth2UserService)
                .oidcUserService(customOidcUserService))
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                        PrincipalUser principalUser = (PrincipalUser) token.getPrincipal();
                        ProviderUser providerUser = principalUser.getProviderUser();

                        String jwt = JWT.create()
                                .withSubject(providerUser.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                                .withClaim("provider", providerUser.getProvider())
                                .withClaim("id", providerUser.getId())
                                .withClaim("picture", providerUser.getPicture())
                                .sign(Algorithm.HMAC512(SECRET));

                        System.out.println(TOKEN_PREFIX + " " +jwt);
                        response.setHeader(HEADER_STRING,TOKEN_PREFIX + " " +jwt);
                    }
                }));



        http.logout()
                .logoutSuccessHandler(oidcLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");


//        http.exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")); // OK
//                .accessDeniedHandler((request,response,exception) -> { // TODO : 상세하게 분기
//                    System.out.println(exception.getMessage());
//                    response.sendError(HttpStatus.FORBIDDEN.value());
//                    response.sendRedirect("/denied");
//                });



        return http.build();
    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("http://localhost:8081/login");
        return successHandler;
    }


    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }
}
