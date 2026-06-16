package com.example.nexobank2.security;

import com.example.nexobank2.service.impl.JwtService;
import com.example.nexobank2.service.impl.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            log.debug("Processing request to: {} with Authorization header: {}", request.getRequestURI(), authHeader != null ? "Present" : "Absent");

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                log.debug("No Bearer token found, continuing filter chain");
                filterChain.doFilter(request,response);
                return;
            }
            
            String token = authHeader.substring(7);
            if(!jwtService.validateToken(token)){
                log.warn("Token validation failed");
                filterChain.doFilter(request,response);
                return;
            }
            
            String email = jwtService.extractEmail(token);
            log.debug("Extracted email from token: {}", email);
            
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailService.loadUserByUsername(email);
                log.debug("Loaded user details for: {}, authorities: {}", email, userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("Authentication set in SecurityContext for user: {}", email);
            }
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage(), e);
        }
        
        filterChain.doFilter(request,response);
    }
}
