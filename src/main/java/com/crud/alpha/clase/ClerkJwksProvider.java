package com.crud.alpha.clase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClerkJwksProvider {

    private static final Logger logger = LoggerFactory.getLogger(ClerkJwksProvider.class);

    @Value("${clerk.jwks-url}")
    private String jwksUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Map<String, Key> keyCache = new ConcurrentHashMap<>();
    private final JwksKeyResolver keyResolver = new JwksKeyResolver();

    public ClerkJwksProvider() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Fetches JWKS from Clerk and updates the key cache
     */
    @Scheduled(fixedRateString = "${clerk.jwks-refresh-rate:3600000}")
    public void refreshJwks() {
        try {
            logger.info("Fetching JWKS from {}", jwksUrl);

            ResponseEntity<String> response = restTemplate.getForEntity(jwksUrl, String.class);
            JsonNode jwks = objectMapper.readTree(response.getBody());

            if (jwks.has("keys") && jwks.get("keys").isArray()) {
                for (JsonNode keyNode : jwks.get("keys")) {
                    try {
                        if (keyNode.has("kid") &&
                                keyNode.has("n") &&
                                keyNode.has("e") &&
                                "RSA".equals(keyNode.get("kty").asText())) {

                            String kid = keyNode.get("kid").asText();
                            String n = keyNode.get("n").asText();
                            String e = keyNode.get("e").asText();

                            // Convert Base64URL to BigInteger
                            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
                            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

                            // Create RSA public key
                            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
                            KeyFactory factory = KeyFactory.getInstance("RSA");
                            Key publicKey = factory.generatePublic(spec);

                            // Cache the key
                            keyCache.put(kid, publicKey);
                            logger.debug("Cached JWT public key with kid: {}", kid);
                        }
                    } catch (Exception e) {
                        logger.error("Error processing JWKS key: {}", e.getMessage(), e);
                    }
                }
            }

            logger.info("JWKS refresh complete, cached {} keys", keyCache.size());
        } catch (Exception e) {
            logger.error("Failed to fetch or parse JWKS: {}", e.getMessage(), e);
        }
    }

    /**
     * Returns the signing key resolver that uses the cached keys
     */
    public SigningKeyResolver getSigningKeyResolver() {
        // Ensure keys are loaded
        if (keyCache.isEmpty()) {
            refreshJwks();
        }

        return keyResolver;
    }

    /**
     * JWT key resolver that gets the appropriate key based on kid
     */
    private class JwksKeyResolver extends SigningKeyResolverAdapter {
        @Override
        public Key resolveSigningKey(JwsHeader header, Claims claims) {
            String kid = header.getKeyId();
            if (kid == null) {
                logger.error("JWT Header missing kid (key ID)");
                return null;
            }

            Key key = keyCache.get(kid);
            if (key == null) {
                logger.warn("No key found for kid: {}, refreshing JWKS", kid);
                refreshJwks();
                key = keyCache.get(kid);
            }

            return key;
        }
    }
}