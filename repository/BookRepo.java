package com.example.booklist.repository;

import com.example.booklist.entity.Book;
import com.example.booklist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Integer> {

    List<Book> findByUserId(Integer userId);
    List<Book> findByUser(User user);
}
