package com.example.booklist.service;

import com.example.booklist.entity.User;
import com.example.booklist.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public User updateUser(Integer id, User updatedUser) {
        User existingUser = getUserById(id);

        existingUser.setFirstname(updatedUser.getFirstname());
        existingUser.setLastname(updatedUser.getLastname());
        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        existingUser.setRole(updatedUser.getRole());

        return userRepo.save(existingUser);
    }

    public User getUserById(Integer id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public void deleteUser(Integer id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepo.deleteById(id);
    }
}

