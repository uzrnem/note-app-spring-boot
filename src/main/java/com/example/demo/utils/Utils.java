package com.example.demo.utils;

import java.security.Key;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Component
public class Utils {
  
  @Value("${spring.jwt.secret}")
  String secret;
  
  @Value("${spring.jwt.exp_time}")
  Integer expTime;

  private Key key;
  
  public Key getKey() {
    if (key == null) {
      key = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
    }
    return key;
  }
  
  public String generateToken(Integer userId) {
    Instant now = Instant.now();
    return Jwts.builder()
      .claim("userId", userId)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(now.plus(expTime, ChronoUnit.SECONDS)))
      .signWith(SignatureAlgorithm.HS256, this.getKey())
      .compact();
  }
  
  public Integer validateToken(String token) throws Throwable {
    Jws<Claims> jwt = Jwts.parser()
      .setSigningKey(this.getKey())
      .parseClaimsJws(token);
    return (Integer)jwt.getBody().get("userId");
  }
}
