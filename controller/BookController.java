package com.example.booklist.controller;

import com.example.booklist.entity.Book;
import com.example.booklist.entity.User;
import com.example.booklist.service.BookService;
import com.example.booklist.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Book> createBook(
            @RequestBody Book book,
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = jwtService.extractUserId(token);

        book.setUser(new User(userId));
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = jwtService.extractUserId(token);

        Book book = bookService.getBookById(id, userId);
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
    public ResponseEntity<List<Book>> getAllBooks(@RequestHeader("Authorization") String token) {
        Integer userId = jwtService.extractUserId(token);
        List<Book> books = bookService.getAllBooksByUser(userId);
        return ResponseEntity.ok(books);
    }

}
