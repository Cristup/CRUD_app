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

//    @PutMapping("/{id}")
//    public ResponseEntity<Book> updateBook(
//            @PathVariable Integer id,
//            @RequestBody Book bookRequest,
//            @RequestHeader("Authorization") String token
//    ) {
//        Integer userId = jwtService.extractUserId(token.substring(7));
//        Book updatedBook = bookService.updateBook(id, bookRequest, userId);
//        return ResponseEntity.ok(updatedBook);
//    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Integer bookId,
            @RequestBody Book updatedBook,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookService.getBookById(bookId, user.getId());

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        book.setRead(updatedBook.getRead());

        bookService.save(book);

        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        bookService.deleteBook(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getBooksForCurrentUser());
    }

}
