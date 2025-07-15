package com.example.gutendex.repository;

import com.example.gutendex.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.languages l = :language")
    List<Book> findByLanguage(String language);
}
