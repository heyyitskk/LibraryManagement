package org.example.library.repository;

import org.example.library.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan save(Loan loan);

    Optional<Loan> findById(String id);

    List<Loan> findByPatronId(String patronId);

    List<Loan> findByBookId(String bookId);
}
