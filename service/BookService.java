package com.example.booklist.service;

import com.example.booklist.entity.Book;
import com.example.booklist.entity.User;
import com.example.booklist.repository.BookRepo;
import com.example.booklist.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepository;
    private final UserRepo userRepository;

    public Book createBook(Book book, User user) {
        book.setUser(user);
        return bookRepository.save(book);
    }

    public List<Book> getBooksByUser(User user) {
        return bookRepository.findByUser(user);
    }

    public Book getBookById(Integer id, Integer userId) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        if (!book.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not own this book");
        }
        return book;
    }

    public Book updateBook(Integer id, Book bookRequest, Integer userId) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (!book.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not own this book");
        }

        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setRead(bookRequest.getRead());

        return bookRepository.save(book);
    }

    public void deleteBook(Integer bookId, Integer userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (!book.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not own this book");
        }

        bookRepository.delete(book);
    }

    public List<Book> getBooksForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        return bookRepository.findAllByUser(currentUser);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }
}
