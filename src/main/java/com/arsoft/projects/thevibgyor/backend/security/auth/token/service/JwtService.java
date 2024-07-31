package com.arsoft.projects.thevibgyor.backend.security.auth.token.service;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.common.util.Base64Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtService {


    //Should be of length which is a multiple of four
    //private static final String SECRET_KEY = "MYSECRETKEYWHICHISOFATLEASTBITINSIZEMYSECRETKEYWHICHISOFATLEASTBITINSIZE";
    private static final String SECRET_KEY = "MYSECRETKEYWHICHISOFATLEASTBITINSIZEMYSECRETKEYWHICHISOFATLEASTBITINSIZE";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(Base64Util.getDecodedString(SECRET_KEY));
    private static final String issuer = "MY_AUTH_APP";


    public String generateAccessToken(User user, String ttlInMillis) {
        long ttl = Long.parseLong(ttlInMillis);
        if (user == null) {
            user = new User("guest", "guest", "guest.com", List.of(Role.GUEST), "sood");
        }
        if (ttl < 300000) {
            log.info("Setting ttl for the token to " + 300000 + " ms.");
            ttl = 300000;
        }
        log.info(String.format("Creating token for user %s and ttl %s", user, ttl));
        return Jwts.builder()
                .subject(String.format("%s", user.getUserId()))
                .issuer(issuer)
                .claim("roles", user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.toList()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(SIGNING_KEY, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        log.error("Claims values are " + String.valueOf(claims));
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(SIGNING_KEY).build().parseSignedClaims(token).getPayload();
    }

    public List<Role> getRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<?> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(role -> Role.valueOf((String) role))
                .collect(Collectors.toList());
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isValidToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception ex) {
            log.error("Exception occurred while validating the token. Cause: " + ex.getMessage());
            return false;
        }
    }
}
