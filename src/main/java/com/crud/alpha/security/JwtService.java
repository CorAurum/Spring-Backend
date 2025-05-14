package com.crud.alpha.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final ClerkJwksProvider jwksProvider;

    @Value("${clerk.issuer}")
    private String expectedIssuer;

    @Value("${clerk.azp}")
    private String authorizedParty;

    public JwtService(ClerkJwksProvider jwksProvider) {
        this.jwksProvider = jwksProvider;
    }

    /**
     * Validates a JWT token
     */
    public boolean validateToken(String token) {
        try {
            // Parse and validate token
            // This will throw exceptions if token is invalid
            Claims claims = getAllClaims(token);

            // Verify required claims
            if (!validateClaims(claims)) {
                return false;
            }

            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation error: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Gets all claims from a token
     */
    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(jwksProvider.getSigningKeyResolver())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gets a specific claim from the token
     */
    public <T> Optional<T> getClaim(String token, String claimName, Class<T> requiredType) {
        try {
            return Optional.ofNullable(getAllClaims(token).get(claimName, requiredType));
        } catch (Exception e) {
            logger.warn("Failed to extract claim {}: {}", claimName, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Gets the user ID from the token
     */
    public String getUserId(String token) {
        return getAllClaims(token).getSubject();
    }

    /**
     * Gets user roles from the token if present
     */
    @SuppressWarnings("unchecked")
    public Optional<List<String>> getRoles(String token) {
        try {
            return Optional.ofNullable(getAllClaims(token).get("roles", List.class));
        } catch (Exception e) {
            logger.warn("Failed to extract roles: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Validates required claims like issuer and audience
     */
    private boolean validateClaims(Claims claims) {
        // Check issuer if expected issuer is configured
        if (expectedIssuer != null && !expectedIssuer.isBlank()) {
            String issuer = claims.getIssuer();
            if (issuer == null || !issuer.equals(expectedIssuer)) {
                logger.warn("Invalid issuer: {}", issuer);
                return false;
            }
        }

        // Check azp (authorized party) instead of aud
        if (authorizedParty != null && !authorizedParty.isBlank()) {
            String azp = claims.get("azp", String.class);
            if (azp == null || !azp.equals(authorizedParty)) {
                logger.warn("Invalid azp: {}", azp);
                return false;
            }
        }
        return true;
    }
}