package com.seproject.oauth2.utils.jwt.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
public class JwtAspect {

    @Pointcut("@annotation(com.seproject.oauth2.utils.jwt.annotation.JWT)")
    private void hasJWT(){}

    @Around("hasJWT()")
    public void checkJWT(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String jwt = request.getHeader("Authorization");
        if(jwt == null) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.sendError(HttpStatus.UNAUTHORIZED.value(),"jwt가 존재하지 않음");
            return;
        }

        joinPoint.proceed();


    }

}
