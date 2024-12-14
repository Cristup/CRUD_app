package com.example.booklist.controller;

import com.example.booklist.entity.User;
import com.example.booklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getAuthenticatedUser(
            @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(authenticatedUser);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateAuthenticatedUser(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestBody User updatedUser) {
        User user = userService.updateUser(authenticatedUser.getId(), updatedUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

}