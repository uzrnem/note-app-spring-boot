package com.example.demo.utils;

import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.core.io.JsonEOFException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Aspect
@Component
public class AutheticateAspect {
    
    @Autowired
    HttpServletRequest request;

    @Autowired
    Utils util;

    @Around("@annotation(com.example.demo.interfaces.Autheticate)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        String token = request.getHeader("Authorization");

        try {
            request.setAttribute("user", util.validateToken(token));

            Object proceed = joinPoint.proceed();

            return proceed;
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("expired");
        } catch (JsonEOFException | MalformedJwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("bad token");
        }
    }
}
