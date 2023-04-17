package com.seproject.oauth2;

import com.seproject.oauth2.repository.AuthorizationMetaDataRepository;
import com.seproject.oauth2.service.CustomOidcUserService;
import com.seproject.oauth2.utils.*;
import com.seproject.oauth2.utils.handler.FormLoginAuthenticationSuccessHandler;
import com.seproject.oauth2.utils.handler.FormLoginFailureHandler;
import com.seproject.oauth2.utils.handler.OidcAuthenticationSuccessHandler;
import com.seproject.oauth2.utils.jwt.JwtDecoder;
import com.seproject.oauth2.utils.jwt.JwtFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
@AllArgsConstructor
@EnableWebSecurity
public class OAuth2ClientConfig {

    private ClientRegistrationRepository clientRegistrationRepository;
    private final CustomOidcUserService customOidcUserService;
    private final OidcAuthenticationSuccessHandler oidcAuthenticationSuccessHandler;
    private final FormLoginAuthenticationSuccessHandler formLoginAuthenticationSuccessHandler;
    private final FormLoginFailureHandler formLoginFailureHandler;

    private AuthorizationMetaDataRepository authorizationMetaDataRepository;
    private JwtDecoder jwtDecoder;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/static/**").antMatchers("/error");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .anyRequest().permitAll();

        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/formLogin")
                .successHandler(formLoginAuthenticationSuccessHandler)
                .failureHandler(formLoginFailureHandler)
                .permitAll();

        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfoEndpointConfig ->
                userInfoEndpointConfig.oidcUserService(customOidcUserService))
                .successHandler(oidcAuthenticationSuccessHandler));

        //formlogin과 oauth2의 통합 로그아웃 처리가 필요
        http.logout()
                .logoutSuccessHandler(oidcLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .deleteCookies("remember-me");

//        http.exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")); // OK
//                .accessDeniedHandler((request,response,exception) -> { // TODO : 상세하게 분기
//                    System.out.println(exception.getMessage());
//                    response.sendError(HttpStatus.FORBIDDEN.value());
//                    response.sendRedirect("/denied");
//                });

        http.addFilterBefore(new JwtFilter(authorizationMetaDataRepository,jwtDecoder), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("http://localhost:8080/login");
        return successHandler;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }

}
