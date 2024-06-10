package com.example.demo.security.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.example.demo.security.AuthencationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${salesup.app.jwtSecret}")
    private String jwtSecret;

    @Value("${salesup.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        return generateJwtToken(authentication, jwtExpirationMs);
    }

    public String generateJwtToken(Authentication authentication, int jwtExpirationMs) {
        AuthencationUser userPrinciple = (AuthencationUser) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrinciple.getLastName()))
                .claim("roles", getListAuthorities(userPrinciple.getAuthorities()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public List<String> getListAuthorities(Collection<? extends GrantedAuthority> listGrantedAuthorities){
        List<String> listAuthorities = new ArrayList<>();
        for (GrantedAuthority authority : listGrantedAuthorities){
            listAuthorities.add(authority.getAuthority());
        }
        return listAuthorities;

    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean ValidateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT clains string is empty: {}", e.getMessage());
        }
        return false;
    }
}
