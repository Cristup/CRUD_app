package com.example.booklist.controller;

import com.example.booklist.dto.AuthenticationRequest;
import com.example.booklist.dto.AuthenticationResponse;
import com.example.booklist.dto.RegisterRequest;
import com.example.booklist.service.AuthenticationService;
import com.example.booklist.service.BlacklistService;
import com.example.booklist.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtService jwtService;
    private final BlacklistService blacklistService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Received token: " + token); // Debugging
            blacklistService.addTokenToBlacklist(token);
        } else {
            System.out.println("Invalid or missing Authorization header");
        }

        return ResponseEntity.ok("Logged out successfully");
    }
}
