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

    public Book updateBook(Integer id, Book updatedBook, Integer userId) {
        Book existingBook = getBookById(id, userId);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setRead(updatedBook.getRead());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Integer id, Integer userId) {
        Book book = getBookById(id, userId);
        bookRepository.delete(book);
    }

    public List<Book> getBooksForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        return bookRepository.findAllByUser(currentUser);
    }

}
