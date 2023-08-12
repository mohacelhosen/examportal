package com.mcu.examportal.service;

import com.mcu.examportal.controller.UserRestController;
import com.mcu.examportal.entity.UserEntity;
import com.mcu.examportal.exception.TokenValidationException;
import com.mcu.examportal.exception.UserNotFoundException;
import com.mcu.examportal.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    Logger logger = LoggerFactory.getLogger(JwtService.class);
    public static final String SECRET="79a9ef48bc06cb4b9c9ac9867a3197fb13a98e0684b478b97cf931b6afdb6ad2";
    @Autowired
    private UserRepository repository;

    // generate token
    public String generateToken(String email) {
        Optional<UserEntity> userEntityOptional = repository.findByEmail(email);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            String roles = userEntity.getRoles();

            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", roles); // Add the roles information to the claims

            return createToken(claims, email);
        } else {
            // Handle the case when the user does not exist in the database
            throw new UserNotFoundException("User not found for email: " + email);
        }
    }



    // create a token
    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*5))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    // VERIFY SIGNATURE (secret key with base64 decode and return HMAC-SHA256)
    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // extract user name/ email
    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // extract expiration time
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUserEmail(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUserEmail(token);

            // Check if the token's subject (email) matches the UserDetails' username
            if (!username.equals(userDetails.getUsername())) {
                return false;
            }

            // Check if the token is expired
            if (isTokenExpired(token)) {
                return false;
            }

            // Additional validation logic, if needed

            return true; // Token is valid
        } catch (Exception e) {
            // Log the error for debugging
            logger.error("Error validating token: " + e.getMessage());

            // Throw a custom exception or return a meaningful error response
            throw new TokenValidationException("Token validation failed: " + e.getMessage());
        }
    }


}
