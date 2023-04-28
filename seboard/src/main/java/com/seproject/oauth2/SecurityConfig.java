package com.seproject.oauth2;

import com.seproject.oauth2.repository.AuthorizationMetaDataRepository;
import com.seproject.oauth2.service.CustomOidcUserService;
import com.seproject.oauth2.utils.*;
import com.seproject.oauth2.utils.handler.*;
import com.seproject.oauth2.utils.jwt.JwtDecoder;
import com.seproject.oauth2.utils.jwt.JwtFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private ClientRegistrationRepository clientRegistrationRepository;
    private final CustomOidcUserService customOidcUserService;
    private final OidcAuthenticationSuccessHandler oidcAuthenticationSuccessHandler;
    private final FormLoginAuthenticationSuccessHandler formLoginAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler formLoginFailureHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private AuthorizationMetaDataRepository authorizationMetaDataRepository;
    private JwtDecoder jwtDecoder;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/static/**").antMatchers("/error");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();

        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/index")
                .loginProcessingUrl("/formLogin")
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

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);

        http.addFilterBefore(new JwtFilter(authorizationMetaDataRepository,jwtDecoder,authenticationFailureHandler), UsernamePasswordAuthenticationFilter.class);
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
