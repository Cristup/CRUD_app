package com.example.booklist.controller;

import com.example.booklist.entity.Book;
import com.example.booklist.entity.User;
import com.example.booklist.repository.UserRepo;
import com.example.booklist.service.BookService;
import com.example.booklist.service.JwtService;
import com.example.booklist.service.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final JwtService jwtService;
    private final UserRepo userRepo;

    @PostMapping
    public ResponseEntity<Book> createBook(
            @RequestBody Book book,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(book, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id, Principal principal) {
        String email = principal.getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookService.getBookById(id, user.getId());

        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Integer id,
            @RequestBody Book book,
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = jwtService.extractUserId(token);
        book.setId(id); // Ensure the ID matches the book we want to update
        Book updatedBook = bookService.updateBook(id, book, userId);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = jwtService.extractUserId(token);
        bookService.deleteBook(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getBooksForCurrentUser());
    }

}
