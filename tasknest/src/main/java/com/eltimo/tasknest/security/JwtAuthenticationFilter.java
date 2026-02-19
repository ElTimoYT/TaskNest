package com.eltimo.tasknest.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.eltimo.tasknest.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.eltimo.tasknest.security.TokenJwtConfig.PREFIX_TOKEN;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(HEADER_AUTHORIZATION);
        System.out.println("1. Cabecera recibida: " + authHeader); // 👈 CHIVATO 1
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith(PREFIX_TOKEN)) {
            System.out.println("2. No hay cabecera o no empieza por Bearer. Ignorando token."); // 👈 CHIVATO 2
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        System.out.println("3. Token extraído. Email del usuario: " + userEmail); // 👈 CHIVATO 3

        if(userEmail == null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                System.out.println("4. Token VÁLIDO. Dando acceso a Spring Security."); // 👈 CHIVATO 4
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else {
                System.out.println("5. EL TOKEN NO ES VÁLIDO"); // 👈 CHIVATO 5
            }
        }
        filterChain.doFilter(request, response);

    }
}
