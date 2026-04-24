package org.example.library.service;

import org.example.library.enums.BookStatus;
import org.example.library.models.Book;
import org.example.library.models.Loan;
import org.example.library.models.Patron;
import org.example.library.repository.BookRepository;
import org.example.library.repository.LoanRepository;
import org.example.library.repository.PatronRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

public class LendingService {
    private static final Logger logger = LoggerFactory.getLogger(LendingService.class);

    private final BookRepository bookRepo;
    private final PatronRepository patronRepo;
    private final LoanRepository loanRepo;
    private final ReservationService reservationService;

    public LendingService(BookRepository bookRepo, PatronRepository patronRepo, LoanRepository loanRepo,
            ReservationService reservationService) {
        this.bookRepo = bookRepo;
        this.patronRepo = patronRepo;
        this.loanRepo = loanRepo;
        this.reservationService = reservationService;
    }

    public Optional<Loan> checkoutByIsbn(String isbn, String patronId) {
        Optional<Book> ob = bookRepo.findByIsbn(isbn);
        if (ob.isEmpty()) {
            logger.warn("Checkout failed: book not found isbn={}", isbn);
            return Optional.empty();
        }
        Book book = ob.get();
        if (book.getStatus() != BookStatus.AVAILABLE) {
            logger.info("Book not available for checkout: {} status={}", isbn, book.getStatus());
            return Optional.empty();
        }

        var patronOpt = patronRepo.findById(patronId);
        if (patronOpt.isEmpty()) {
            logger.warn("Checkout failed: patron not found id={}", patronId);
            return Optional.empty();
        }

        book.setStatus(BookStatus.BORROWED);
        bookRepo.save(book);
        LocalDate now = LocalDate.now();
        Loan loan = new Loan(book.getId(), patronOpt.get().getId(), now, now.plusWeeks(2));
        loanRepo.save(loan);
        patronOpt.get().addToHistory("Checked out: " + book.getTitle() + " (" + isbn + ")");
        patronRepo.save(patronOpt.get());
        logger.info("Book checked out: isbn={} to patron={}", isbn, patronId);
        return Optional.of(loan);
    }

    public boolean returnByIsbn(String isbn, String patronId) {
        Optional<Book> ob = bookRepo.findByIsbn(isbn);
        if (ob.isEmpty()) {
            logger.warn("Return failed: book not found isbn={}", isbn);
            return false;
        }
        Book book = ob.get();
        book.setStatus(BookStatus.AVAILABLE);
        bookRepo.save(book);

        // mark loan returnDate (simplified: pick latest loan for book)
        loanRepo.findByBookId(book.getId().toString()).stream()
                .max((a, b) -> a.getCheckoutDate().compareTo(b.getCheckoutDate())).ifPresent(loan -> {
                    loan.setReturnDate(LocalDate.now());
                    loanRepo.save(loan);
                });

        logger.info("Book returned: isbn={} by patron={}", isbn, patronId);

        // notify reservation system
        reservationService.notifyNextAvailable(book);
        return true;
    }
}
