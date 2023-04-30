package com.seproject.account;

import com.seproject.account.authentication.entrypoint.CustomAuthenticationEntryPoint;
import com.seproject.account.authentication.handler.failure.CustomAuthenticationFailureHandler;
import com.seproject.account.authentication.handler.success.FormLoginAuthenticationSuccessHandler;
import com.seproject.account.authentication.handler.success.OidcAuthenticationSuccessHandler;
import com.seproject.account.authorize.url.UrlFilterInvocationSecurityMetaDataSource;
import com.seproject.account.authorize.url.UrlResourcesFactoryBean;
import com.seproject.account.authorize.handler.CustomAccessDeniedHandler;
import com.seproject.account.service.CustomOidcUserService;
import com.seproject.account.utils.*;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.filter.JwtFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;


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
    private final AuthenticationConfiguration authenticationConfiguration;
    private UrlResourcesFactoryBean urlResourcesFactoryBean;
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

        http.addFilterBefore(new JwtFilter(jwtDecoder,authenticationFailureHandler), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(filterSecurityInterceptor(),FilterSecurityInterceptor.class);
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
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {

        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetaDataSource());
        filterSecurityInterceptor.setAccessDecisionManager(new AffirmativeBased(Arrays.asList(new RoleVoter())));
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());

        return filterSecurityInterceptor;
    }

    @Bean
    public UrlFilterInvocationSecurityMetaDataSource urlFilterInvocationSecurityMetaDataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetaDataSource(urlResourcesFactoryBean);
    }

}
