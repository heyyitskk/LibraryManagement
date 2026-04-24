package org.example.library.service;

import org.example.library.models.Book;

import java.util.*;

public class ReservationService {
    // isbn -> queue of patronIds
    private final Map<String, Queue<String>> reservations = new HashMap<>();
    private final Map<String, NotificationListener> listeners = new HashMap<>();

    public void reserve(String isbn, String patronId) {
        reservations.computeIfAbsent(isbn, k -> new ArrayDeque<>()).add(patronId);
    }

    public Optional<String> nextReservation(String isbn) {
        Queue<String> q = reservations.get(isbn);
        if (q == null || q.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(q.peek());
    }

    public void registerListener(String patronId, NotificationListener listener) {
        listeners.put(patronId, listener);
    }

    public void notifyNextAvailable(Book book) {
        Queue<String> q = reservations.get(book.getIsbn());
        if (q == null)
            return;
        String next = q.poll();
        if (next == null)
            return;
        NotificationListener l = listeners.get(next);
        if (l != null)
            l.onBookAvailable(book, next);
    }
}
