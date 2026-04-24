package org.example.library.repository;

import org.example.library.models.Loan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLoanRepository implements LoanRepository {
    private final Map<String, Loan> byId = new ConcurrentHashMap<>();

    @Override
    public Loan save(Loan loan) {
        byId.put(loan.getId().toString(), loan);
        return loan;
    }

    @Override
    public Optional<Loan> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public List<Loan> findByPatronId(String patronId) {
        List<Loan> res = new ArrayList<>();
        byId.values().forEach(l -> {
            if (l.getPatronId().toString().equals(patronId))
                res.add(l);
        });
        return res;
    }

    @Override
    public List<Loan> findByBookId(String bookId) {
        List<Loan> res = new ArrayList<>();
        byId.values().forEach(l -> {
            if (l.getBookId().toString().equals(bookId))
                res.add(l);
        });
        return res;
    }
}
