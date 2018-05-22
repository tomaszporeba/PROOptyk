package com.prooptykwebapi.prooptyk.security;

import antlr.StringUtils;
import com.prooptykwebapi.prooptyk.model.JwtResponse;
import com.prooptykwebapi.prooptyk.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

import static com.prooptykwebapi.prooptyk.security.SecurityConstants.EXPIRATION_TIME;
import static com.prooptykwebapi.prooptyk.security.SecurityConstants.SECRET;
import static com.prooptykwebapi.prooptyk.security.SecurityConstants.TOKEN_PREFIX;

@Component
public class JWTTokenClaims implements Serializable {

    @Autowired
    UserRepository userRepository;

    public Date getExpirationDateFromClaims(Claims claims) {
        Date expiration;
            expiration = claims.getExpiration();

        return expiration;
    }

    public String getUsernameFromClaims(Claims claims) {
        String username;
        try {
            username = claims.getSubject();

        } catch (ExpiredJwtException ex) {
            username = null;
        }
        return username;
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }

    public JwtResponse generateToken(Authentication authentication) {
        Date expiration = new Date((System.currentTimeMillis() + EXPIRATION_TIME));
        JwtResponse jwtResponse = new JwtResponse();
        String token = Jwts.builder()
                .setSubject(((User) authentication.getPrincipal()).getUsername())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();

        jwtResponse.setAccess_token(token);
        jwtResponse.setExpires_in(expiration);
        jwtResponse.setToken_type("Bearer");
        return jwtResponse;
    }

    public String regenerateToken(String username, Claims claims) {
        Date expiration = new Date((System.currentTimeMillis() + EXPIRATION_TIME));
        long tokenExpiration = this.getExpirationDateFromClaims(claims).getTime();
        boolean isUsernameValid = this.isUsernameValid(username, tokenExpiration);

        if (isUsernameValid) {
            String token = Jwts.builder()
                    .setSubject(username)
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                    .compact();
            return token;
        }

        return null;
    }

    private boolean isUsernameValid (String username, long tokenExpiration) {
        com.prooptykwebapi.prooptyk.model.User user = userRepository.findByUsername(username);

        if (user != null) {
            long lastPasswordChange = user.getLastPasswordChange().getTime();
            if (lastPasswordChange < tokenExpiration) {
                return true;
            } else return false;
        }
        return true;
    }

    //    public String createRefreshToken(Authentication authentication) {
//        if ((((User) authentication.getPrincipal()).getUsername()) {
//            throw new IllegalArgumentException("Cannot create JWT Token without username");
//        }
//        Date expiration = new Date((System.currentTimeMillis() + EXPIRATION_TIME));
//        Date currentTime = new Date();
//
//        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
//        claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
//
//
//        String token = Jwts.builder()
//                .setClaims(claims)
////                .setIssuer(settings.getTokenIssuer())
//                .setId(UUID.randomUUID().toString())
//                .setIssuedAt(currentTime)
//                .setExpiration(expiration)
//                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
//                .compact();
//
//        return new AccessJwtToken(token, claims);
//    }

    public boolean tokenValidate(Claims claims) {

        Date tokenExpiration = this.getExpirationDateFromClaims(claims);
        if (tokenExpiration.getTime() > new Timestamp(System.currentTimeMillis()).getTime()) {
            return true;
        } else {return false;}

    }
}
