package com.prooptykwebapi.prooptyk.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.prooptykwebapi.prooptyk.security.SecurityConstants.HEADER_STRING;
import static com.prooptykwebapi.prooptyk.security.SecurityConstants.SECRET;
import static com.prooptykwebapi.prooptyk.security.SecurityConstants.TOKEN_PREFIX;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JWTTokenClaims jwtTokenClaims;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTTokenClaims jwtTokenClaims) {
        super(authenticationManager);
        this.jwtTokenClaims = jwtTokenClaims;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request, response);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String token = null;
        try {
            token = request.getHeader(HEADER_STRING);
        } catch (Exception e) {}

        String username;
        Claims claims;

        if (token !=null) {
            claims  = jwtTokenClaims.getClaimsFromToken(token);
            username = jwtTokenClaims.getUsernameFromClaims(claims);
            boolean tokenValidate = jwtTokenClaims.tokenValidate(claims);

            if (tokenValidate) {
                if (username != null) {
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                } return null;
            } else {
                token = jwtTokenClaims.regenerateToken(username, claims);
                if (token != null) {
                    response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                } return null;

            }

        }
        return null;
    }

}
