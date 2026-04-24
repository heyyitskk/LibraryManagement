package org.example.library.service;

import org.example.library.models.Book;
import org.example.library.models.Patron;
import org.example.library.repository.BookRepository;
import org.example.library.repository.PatronRepository;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationService {
    private final BookRepository bookRepo;
    private final PatronRepository patronRepo;

    public RecommendationService(BookRepository bookRepo, PatronRepository patronRepo) {
        this.bookRepo = bookRepo;
        this.patronRepo = patronRepo;
    }

    // simple recommendation: recommend books by most-read authors in patron history
    public List<Book> recommend(String patronId, int max) {
        Optional<Patron> pOpt = patronRepo.findById(patronId);
        if (pOpt.isEmpty())
            return Collections.emptyList();
        Patron p = pOpt.get();
        Map<String, Integer> authorCount = new HashMap<>();
        for (String entry : p.getBorrowingHistory()) {
            // naive parse: assume format contains author in parentheses? fallback to title
            // parsing
            // For this simple impl, count author occurrences by checking all books the
            // patron borrowed
            bookRepo.findAll().forEach(b -> {
                if (entry.contains(b.getTitle()))
                    authorCount.merge(b.getAuthor(), 1, Integer::sum);
            });
        }
        List<String> favoriteAuthors = authorCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        List<Book> result = new ArrayList<>();
        for (String author : favoriteAuthors) {
            for (Book b : bookRepo.findByAuthor(author)) {
                if (result.size() >= max)
                    break;
                result.add(b);
            }
            if (result.size() >= max)
                break;
        }
        // fallback: return some available books
        if (result.isEmpty()) {
            result = bookRepo.findAll().stream().limit(max).collect(Collectors.toList());
        }
        return result;
    }
}
