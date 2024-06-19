package com.microverse.users.security.jwtservices;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
	
    @Value("${microverse.app.jwtSecret}")
    private String secretKey;
    
    @Value("${microverse.app.jwtSecretRefresh}")
    private String refreshSecretKey;

    @Value("${microverse.app.jwtExpirationMs}")
    private String jwtExpiration;
    
    @Value("${microverse.app.jwtRefreshExpirationMs}")
    private String jwtExpirationRefresh;
    
    
    

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, Long.valueOf(jwtExpiration).longValue());
    }
    
  

    public long getExpirationTime() {
        return Long.valueOf(jwtExpiration).longValue();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
    	
    	
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    private Key getSignInKeyRefresh() {
        byte[] keyBytes = Decoders.BASE64.decode(refreshSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    
    public String generateRefreshToken(UserDetails userDetails) {
    	Map<String, Object> extraClaims = new HashMap<>();;
    	extraClaims.put("jti", UUID.randomUUID().toString()); // Add unique identifier for refresh token
    	extraClaims.put("type", "refresh"); // Additional claim to indicate refresh token
    	extraClaims.put("extra_security", UUID.randomUUID().toString().replace("-", "")); // More entropy
        return buildRefereshFromAccessToken(extraClaims, userDetails, Long.valueOf(jwtExpirationRefresh).longValue()); // 7 days
    }
    
    private String buildRefereshFromAccessToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKeyRefresh(), SignatureAlgorithm.HS256)
                .compact();
    }
}
