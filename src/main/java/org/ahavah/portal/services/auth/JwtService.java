package org.ahavah.portal.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.access_expiry}")
    private Long access_expiry;
    @Value("${jwt.refresh_expiry}")
    private Long refresh_expiry;

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(Long id, String email, String role, String division){

        return generateTk(id, email, role.toUpperCase(), division, access_expiry);
    }
    public String generateRefreshToken(Long id, String email, String role, String division){

        return generateTk(id, email, role, division, refresh_expiry);
    }

    private String generateTk(Long id, String email, String role, String division, Long time) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("id", id)
                .claim("email", email)
                .claim("division", division)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .expiration(new Date(System.currentTimeMillis() + time * 1000))
                .compact();
    }

    public boolean validateToken(String token){
    try {

        var claims = getClaims(token);

        return claims.getExpiration().after(new Date());
    }
    catch (Exception e) {
        return false;
    }

    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        var idObj = getClaims(token).get("id");
        if (idObj instanceof Number) {
            return ((Number) idObj).longValue();
        }
        if (idObj != null) {
            return Long.valueOf(idObj.toString());
        }
        return null;
    }

    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }




}
