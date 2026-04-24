package org.example.library.repository;

import org.example.library.models.Patron;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPatronRepository implements PatronRepository {
    private final Map<String, Patron> byId = new ConcurrentHashMap<>();

    @Override
    public Patron save(Patron patron) {
        byId.put(patron.getId().toString(), patron);
        return patron;
    }

    @Override
    public Optional<Patron> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public List<Patron> findAll() {
        return new ArrayList<>(byId.values());
    }
}
