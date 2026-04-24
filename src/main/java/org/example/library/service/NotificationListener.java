package org.example.library.service;

import org.example.library.models.Book;

public interface NotificationListener {
    void onBookAvailable(Book book, String patronId);
}
