package com.example.gutendex.service;

import com.example.gutendex.model.Author;
import com.example.gutendex.model.Book;
import com.example.gutendex.model.GutendexResponse;
import com.example.gutendex.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseService {
    private final BookService bookService;
    private final AuthorRepository authorRepository;

    public DatabaseService(BookService bookService, AuthorRepository authorRepository) {
        this.bookService = bookService;
        this.authorRepository = authorRepository;
    }

    public Book processAndSaveBook(GutendexResponse.BookResult bookResult) {
        Book book = new Book();
        book.setTitle(bookResult.getTitle());
        book.setDownloadCount(bookResult.getDownloadCount());
        book.setLanguages(bookResult.getLanguages());

        List<Author> authors = bookResult.getAuthors().stream()
                .map(authorResult -> {
                    Author author = new Author();
                    author.setName(authorResult.getName());
                    author.setBirthYear(authorResult.getBirthYear());
                    author.setDeathYear(authorResult.getDeathYear());
                    return author;
                })
                .collect(Collectors.toList());
        book.setAuthors(authors);

        return bookService.saveBook(book);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> getAuthorsAliveInYear(Integer year) {
        return authorRepository.findAuthorsAliveInYear(year);
    }
}
