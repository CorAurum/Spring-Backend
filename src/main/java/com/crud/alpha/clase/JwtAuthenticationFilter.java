package com.crud.alpha.clase;

import com.crud.alpha.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // Extract authorization header
            String authHeader = request.getHeader("Authorization");

            // Skip filter if no Authorization header or not a Bearer token
            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract token (remove "Bearer " prefix)
            String token = authHeader.substring(BEARER_PREFIX.length());

            // Validate token and get claims
            if (jwtService.validateToken(token)) {
                // Extract user details from token
                String userId = jwtService.getUserId(token);
                Optional<List<String>> roles = jwtService.getRoles(token);

                // Convert roles to Spring Security authorities
                List<SimpleGrantedAuthority> authorities = roles
                        .orElse(List.of())
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Create authentication object
                var authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null, // credentials (not needed here)
                        authorities
                );

                // Add request details to authentication
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Successfully authenticated user: {}", userId);
            } else {
                logger.warn("Invalid JWT token");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }
}