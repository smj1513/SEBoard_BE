package com.seproject.oauth2;

import com.seproject.oauth2.service.CustomOAuth2UserService;
import com.seproject.oauth2.service.CustomOidcUserService;
import com.seproject.oauth2.utils.CustomAuthorityMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpSession;

@AllArgsConstructor
@EnableWebSecurity
public class OAuth2ClientConfig {

    private final CustomOidcUserService customOidcUserService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring().antMatchers("/static/**").antMatchers("/error");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/user").access("hasAnyRole('SCOPE_profile','SCOPE_email')")
                .antMatchers("/api/oidc").access("hasAnyRole('SCOPE_openid')")
                .anyRequest().authenticated();

//        http.formLogin().loginPage("/login").loginProcessingUrl("/loginProc").permitAll();
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService)
                .oidcUserService(customOidcUserService)));
//        http.exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")); // OK
//                .accessDeniedHandler((request,response,exception) -> { // TODO : 상세하게 분기
//                    System.out.println(exception.getMessage());
//                    response.sendError(HttpStatus.FORBIDDEN.value());
//                    response.sendRedirect("/denied");
//                });
//
//        http.logout()
//                .logoutUrl("/logout")
//                .addLogoutHandler((request,response,authentication) ->{
//                    HttpSession session = request.getSession();
//                    session.invalidate();
//                })
//                .logoutSuccessHandler((request,response,authentication) -> {
//                    response.setStatus(HttpStatus.OK.value());
//                    //response.sendRedirect("/login");
//                })
//                .deleteCookies("remember-me");

        return http.build();
    }


    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }
}
