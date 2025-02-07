package com.thcsOauth2.AutenticatorDemo.service;

import com.thcsOauth2.AutenticatorDemo.dto.AuthResponse;
import com.thcsOauth2.AutenticatorDemo.dto.LoginRequest;
import com.thcsOauth2.AutenticatorDemo.dto.RegisterRequest;
import com.thcsOauth2.AutenticatorDemo.model.User;
import com.thcsOauth2.AutenticatorDemo.repository.UserRepository;
import com.thcsOauth2.AutenticatorDemo.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;

    public AuthResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNome(request.getNome());
        user.setCognome(request.getCognome());

        userRepository.save(user);
        
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail());
    }

    public AuthResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password non valida");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail());
    }
} 