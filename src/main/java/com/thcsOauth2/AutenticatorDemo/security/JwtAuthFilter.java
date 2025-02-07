package com.thcsOauth2.AutenticatorDemo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtro per l'autenticazione JWT
 * Intercetta tutte le richieste HTTP e verifica la presenza e validità del token JWT
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        // Estrae il token dall'header Authorization (formato: "Bearer <token>")
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Se non c'è il token o non inizia con "Bearer ", passa al prossimo filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrae il token rimuovendo "Bearer "
        jwt = authHeader.substring(7);
        // Estrae l'email dal token
        userEmail = jwtService.extractEmail(jwt);

        // Se l'email è presente e l'utente non è già autenticato
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Verifica la validità del token
            if (jwtService.isTokenValid(jwt)) {
                // Crea un oggetto di autenticazione
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userEmail,    // Principal (email dell'utente)
                    null,         // Credentials (non necessarie qui)
                    new ArrayList<>()  // Authorities (ruoli dell'utente)
                );
                // Aggiunge dettagli della richiesta
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Imposta l'autenticazione nel contesto di sicurezza
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Passa al prossimo filtro
        filterChain.doFilter(request, response);
    }
} 