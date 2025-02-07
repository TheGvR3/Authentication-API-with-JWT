package com.thcsOauth2.AutenticatorDemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servizio per la gestione dei JWT (JSON Web Token)
 * 
 * JWT è un formato standard per trasmettere informazioni in modo sicuro tra client e server.
 * È composto da tre parti: Header, Payload e Signature, separate da punti.
 * Esempio: xxxxx.yyyyy.zzzzz
 */
@Service
public class JwtService {
    // Chiave segreta usata per firmare il token. In produzione, dovrebbe essere in un file di configurazione sicuro
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    
    /**
     * Estrae l'email dal token JWT
     * L'email è memorizzata nel campo "subject" del token
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Metodo generico per estrarre qualsiasi claim dal token
     * I claims sono le informazioni contenute nel payload del token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Genera un token JWT contenente solo l'email dell'utente
     */
    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }
    
    /**
     * Genera un token JWT completo con claims personalizzati
     * @param extraClaims - Map di claims aggiuntivi da includere nel token
     * @param email - Email dell'utente (memorizzata nel subject)
     * @return Token JWT firmato
     */
    public String generateToken(Map<String, Object> extraClaims, String email) {
        return Jwts
                .builder()
                .setClaims(extraClaims)           // Aggiunge claims personalizzati
                .setSubject(email)                // Imposta l'email come subject
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Data di emissione
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))  // Scadenza: 24 ore
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // Firma il token con HS256
                .compact();  // Costruisce il token finale
    }
    
    /**
     * Verifica se il token è valido (non scaduto)
     */
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }
    
    /**
     * Verifica se il token è scaduto confrontando la data di scadenza con quella attuale
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Estrae la data di scadenza dal token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Estrae tutti i claims dal token
     * Verifica anche la firma del token usando la chiave segreta
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())  // Imposta la chiave per verificare la firma
                .build()
                .parseClaimsJws(token)  // Analizza e verifica il token
                .getBody();  // Ottiene il payload
    }
    
    /**
     * Genera la chiave di firma dal SECRET_KEY
     * Converte la stringa esadecimale in una chiave utilizzabile per la firma
     */
    private Key getSignInKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
} 