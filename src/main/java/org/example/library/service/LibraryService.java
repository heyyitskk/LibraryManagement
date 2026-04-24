package org.example.library.service;

import org.example.library.factory.EntityFactory;
import org.example.library.models.Book;
import org.example.library.models.Patron;
import org.example.library.repository.BookRepository;
import org.example.library.repository.InMemoryBookRepository;
import org.example.library.repository.InMemoryLoanRepository;
import org.example.library.repository.InMemoryPatronRepository;
import org.example.library.repository.LoanRepository;
import org.example.library.repository.PatronRepository;

import java.util.List;
import java.util.Optional;

public class LibraryService {
    private final BookRepository bookRepo;
    private final PatronRepository patronRepo;
    private final LoanRepository loanRepo;
    private final ReservationService reservationService;
    private final LendingService lendingService;
    private final RecommendationService recommendationService;

    public LibraryService() {
        this.bookRepo = new InMemoryBookRepository();
        this.patronRepo = new InMemoryPatronRepository();
        this.loanRepo = new InMemoryLoanRepository();
        this.reservationService = new ReservationService();
        this.lendingService = new LendingService(bookRepo, patronRepo, loanRepo, reservationService);
        this.recommendationService = new RecommendationService(bookRepo, patronRepo);
    }

    // Book operations
    public Book addBook(String title, String author, String isbn, int year) {
        Book b = EntityFactory.createBook(title, author, isbn, year);
        return bookRepo.save(b);
    }

    public void removeBook(String isbn) {
        bookRepo.deleteByIsbn(isbn);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepo.findByIsbn(isbn);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepo.findByTitle(title);
    }

    public List<Book> searchByAuthor(String author) {
        return bookRepo.findByAuthor(author);
    }

    // Patron operations
    public Patron addPatron(String name, String email) {
        Patron p = EntityFactory.createPatron(name, email);
        return patronRepo.save(p);
    }

    public Optional<Patron> findPatron(String id) {
        return patronRepo.findById(id);
    }

    // Lending
    public Optional<?> checkout(String isbn, String patronId) {
        return lendingService.checkoutByIsbn(isbn, patronId);
    }

    public boolean returnBook(String isbn, String patronId) {
        return lendingService.returnByIsbn(isbn, patronId);
    }

    // Reservations
    public void reserve(String isbn, String patronId) {
        reservationService.reserve(isbn, patronId);
    }

    public void registerReservationListener(String patronId, NotificationListener listener) {
        reservationService.registerListener(patronId, listener);
    }

    // Recommendations
    public List<?> recommend(String patronId, int max) {
        return recommendationService.recommend(patronId, max);
    }
}
