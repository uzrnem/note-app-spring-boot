package com.example.demo.utils;

import java.security.Key;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

@Component
public class Utils {
  
  @Value("${spring.jwt.secret}")
  String secret;

  private Key key;
  
  public Utils() {
    if (myStr2.isEmpty()) {
      secret = "default";
    }
    key = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
  }
  
  public Key getJwtKey() {
    return this.key;
  }
}
