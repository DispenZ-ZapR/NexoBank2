package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Slf4j
@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.token.time}")
    private Long time;
    private SecretKey key;

    public SecretKey getKey(){
        if(Objects.isNull(key)){
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            key = Keys.hmacShaKeyFor(keyBytes);
        }
        return key;
    }

    public String generateToken(UserDetails userDetails){
        try {
            User user = (User) userDetails;
            Date now = new Date();
            Date expiry = new Date(now.getTime() + time);
            Map<String,Object> claims = new HashMap<>();
            claims.put("ID", user.getId());
            claims.put("EMAIL", userDetails.getUsername());
            claims.put("ROLE", user.getUserType());
            
            String token = Jwts.builder()
                    .claims(claims)
                    .subject(user.getUsername())
                    .issuedAt(now)
                    .expiration(expiry)
                    .signWith(getKey())
                    .compact();
            
            log.debug("Сгенерирован JWT токен для пользователя: {}", userDetails.getUsername());
            return token;
        } catch (Exception e) {
            log.error("Ошибка при генерации JWT токена для пользователя {}: {}", userDetails.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Не удалось сгенерировать JWT токен", e);
        }
    }

    public String extractEmail(String token){
        try {
            String email = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            log.debug("Извлечен email из JWT токена: {}", email);
            return email;
        } catch (JwtException e) {
            log.error("Ошибка при извлечении email из JWT токена: {}", e.getMessage(), e);
            throw e;
        }
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(getKey()).build()
                    .parseSignedClaims(token);
            log.debug("JWT токен успешно валидирован");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT токен истек: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("Неподдерживаемый JWT токен: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Некорректный формат JWT токена: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.warn("Неверная подпись JWT токена: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("JWT токен пустой или содержит только пробелы: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("Ошибка валидации JWT токена: {}", e.getMessage(), e);
            return false;
        }
    }
}
