package com.example.demo.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.example.demo.interfaces.Autheticate;
import com.example.demo.schema.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.Method;
import org.aspectj.lang.reflect.MethodSignature;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@Aspect
@Component
public class AutheticateAspect {
    
    @Autowired
    HttpServletRequest request;

    @Autowired
    Utils util;

    @Around("@annotation(com.example.demo.interfaces.Autheticate)")
    public Object authenticateToken(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Autheticate authAnnotation = method.getAnnotation(Autheticate.class);
        String[] requiredRoles = authAnnotation.roles();

        String token = request.getHeader("Authorization");

        try {
            if (token == null || token.replace("Bearer ", "").equals("")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response<>("empty token!", null));
            }
            Map<String, Object> user = util.validateToken(token.replace("Bearer ", ""));
            request.setAttribute("user_id", (Integer) user.get("id"));

            if (requiredRoles.length > 0) {
                //and role not in user
            }

            Object proceed = joinPoint.proceed();

            return proceed;
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response<>("token expired!", null));
        } catch (JsonEOFException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response<>("token invalid!", null));
        }
    }
}
