package com.seproject.account.filter;

import com.seproject.account.authorize.handler.CustomAccessDeniedHandler;
import com.seproject.account.service.IpService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class IpFilter extends OncePerRequestFilter {
    private final IpService ipService;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String address = getClientIP(request);

        if(ipService.existIpAddress(address)) {
            accessDeniedHandler.handle(request,response,new CustomAccessDeniedException(ErrorCode.BANNED_IP,null));
        } else {
            filterChain.doFilter(request,response);
        }
    }

        public static String getClientIP(HttpServletRequest request) {
            String ip = request.getHeader("X-Forwarded-For");
            log.info("> X-FORWARDED-FOR : " + ip);

            if (ip == null) {
                ip = request.getHeader("Proxy-Client-IP");
                log.info("> Proxy-Client-IP : " + ip);
            }
            if (ip == null) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                log.info(">  WL-Proxy-Client-IP : " + ip);
            }
            if (ip == null) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                log.info("> HTTP_CLIENT_IP : " + ip);
            }
            if (ip == null) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                log.info("> HTTP_X_FORWARDED_FOR : " + ip);
            }
            if (ip == null) {
                ip = request.getRemoteAddr();
                log.info("> getRemoteAddr : "+ip);
            }
            log.info("> Result : IP Address : "+ip);

            return ip;
        }
}
