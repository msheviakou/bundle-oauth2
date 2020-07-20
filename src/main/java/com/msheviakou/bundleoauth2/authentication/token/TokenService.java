package com.msheviakou.bundleoauth2.authentication.token;

import com.msheviakou.bundleoauth2.authentication.jwt.JwtUserDetailsService;
import com.msheviakou.bundleoauth2.exception.AuthenticationException;
import com.msheviakou.bundleoauth2.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.accessTokenSecretKey}")
    private String accessTokenSecretKey;

    @Value("${jwt.refreshTokenSecretKey}")
    private String refreshTokenSecretKey;

    @Value("${jwt.accessTokenValidityInMilliseconds}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refreshTokenValidityInMilliseconds}")
    private long refreshTokenValidityInMilliseconds;

    private final JwtUserDetailsService jwtUserDetailsService;

    @PostConstruct
    protected void init() { accessTokenSecretKey = Base64.getEncoder().encodeToString(accessTokenSecretKey.getBytes(UTF_8)); }

    public Token createToken(User user) {
        return Token.builder()
                .accessToken(createAccessToken(user))
                .refreshToken(createRefreshToken(user))
                .expiresIn(expiration(accessTokenValidityInMilliseconds))
                .build();
    }

    public Authentication getAuthentication(AuthenticationToken token) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(token.getEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public AuthenticationToken resolveToken(HttpServletRequest request) throws AuthenticationException {
        String bearer = "Bearer ";
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            throw new AuthenticationException("Authorization header should be present");
        }
        if (!bearerToken.startsWith(bearer)) {
            throw new AuthenticationException("Authorization header should begin with Bearer");
        }

        return new AuthenticationToken(bearerToken.substring(bearer.length()), accessTokenSecretKey);
    }

    public String getEmailFromRefreshToken(String token) throws JwtException {
        return Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(token).getBody().getSubject();
    }

    private String createAccessToken(User user) {
        return createToken(user, expiration(accessTokenValidityInMilliseconds), accessTokenSecretKey);
    }

    private String createRefreshToken(User user) {
        return createToken(user, expiration(refreshTokenValidityInMilliseconds), refreshTokenSecretKey);
    }

    private String createToken(User user, long expiresIn, String key) {
        Claims claims = Jwts.claims();

        claims.setSubject(user.getEmail());
        claims.put("fullName", String.join(" ", user.getFirstName(), user.getLastName()));
        claims.put("createdAt", user.getCreatedAt());
        claims.put("role", jwtUserDetailsService.getRoleNames(user.getRoles()));
        Date now = new Date();
        Date expirationDate = new Date(expiresIn);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    private long expiration(long validity) {
        return new Date().getTime() + validity;
    }
}
