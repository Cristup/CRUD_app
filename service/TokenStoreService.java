package com.example.booklist.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenStoreService {
    private final Map<Integer, String> validTokens = new HashMap<>();

    public void storeToken(Integer userId, String token) {
        validTokens.put(userId, token);
    }

    public boolean isTokenValid(Integer userId, String token) {
        return token.equals(validTokens.get(userId));
    }

    public void removeToken(Integer userId) {
        validTokens.remove(userId);
    }
}
