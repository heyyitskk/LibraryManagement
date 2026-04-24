package org.example.library.repository;

import org.example.library.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBookRepository implements BookRepository {
    private final Map<String, Book> byIsbn = new ConcurrentHashMap<>();

    @Override
    public Book save(Book book) {
        byIsbn.put(book.getIsbn(), book);
        return book;
    }

    @Override
    public Optional<Book> findById(String id) {
        return byIsbn.values().stream().filter(b -> b.getId().toString().equals(id)).findFirst();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(byIsbn.get(isbn));
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> res = new ArrayList<>();
        byIsbn.values().forEach(b -> {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase()))
                res.add(b);
        });
        return res;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> res = new ArrayList<>();
        byIsbn.values().forEach(b -> {
            if (b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                res.add(b);
        });
        return res;
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(byIsbn.values());
    }

    @Override
    public void deleteByIsbn(String isbn) {
        byIsbn.remove(isbn);
    }
}
