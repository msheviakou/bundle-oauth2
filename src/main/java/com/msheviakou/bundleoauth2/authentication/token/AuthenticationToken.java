package com.msheviakou.bundleoauth2.authentication.token;

import com.msheviakou.bundleoauth2.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class AuthenticationToken {

    private final Jws<Claims> claims;

    AuthenticationToken(String token, String accessTokenSecretKey) throws TokenException {
        try {
            claims = Jwts.parser()
                    .setSigningKey(accessTokenSecretKey)
                    .parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenException("JWT token invalid or expired");
        }
    }

    public boolean isExpired() {
        return claims.getBody().getExpiration().before(new Date());
    }

    public String getEmail() throws JwtException { return claims.getBody().getSubject(); }
}
