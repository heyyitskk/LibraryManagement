package org.example.library.repository;

import org.example.library.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findById(String id);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    void deleteByIsbn(String isbn);
}
