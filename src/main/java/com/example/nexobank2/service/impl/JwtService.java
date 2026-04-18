package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

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
        User user = (User) userDetails;
        Date now = new Date();
        Date expiry = new Date(now.getTime() + time);
        Map<String,Object> claims = new HashMap<>();
        claims.put("ID", user.getId());
        claims.put("EMAIL", userDetails.getUsername());
        claims.put("ROLE", user.getUserType());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getKey())
                .compact();
    }

    public String extractEmail(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(getKey()).build()
                    .parseSignedClaims(token);
            return true;
        }catch (JwtException e){

        }
        return false;
    }
}
