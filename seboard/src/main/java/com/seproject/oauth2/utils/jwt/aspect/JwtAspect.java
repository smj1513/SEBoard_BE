package com.seproject.oauth2.utils.jwt.aspect;

import com.seproject.oauth2.utils.jwt.JwtDecoder;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

@Aspect
public class JwtAspect {

    @Pointcut("@annotation(com.seproject.oauth2.utils.jwt.annotation.JWT)")
    private void hasJWT(){}

    @Before("hasJWT()")
    public void checkJWT(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        boolean flag = false;
        for (int i = 0; i < args.length; i++) {
            if(args[i] instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) args[i];
                String jwt = request.getHeader("Authorization");
                flag |= jwt != null;
            }
        }

        if(!flag) throw new AuthenticationException("jwt가 존재하지 않음");
    }

}
