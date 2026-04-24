package org.example.library.repository;

import org.example.library.models.Patron;

import java.util.List;
import java.util.Optional;

public interface PatronRepository {
    Patron save(Patron patron);

    Optional<Patron> findById(String id);

    List<Patron> findAll();
}
