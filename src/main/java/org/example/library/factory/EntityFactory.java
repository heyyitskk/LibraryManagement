package org.example.library.factory;

import org.example.library.models.Book;
import org.example.library.models.Patron;

public final class EntityFactory {
    private EntityFactory() {
    }

    public static Book createBook(String title, String author, String isbn, int year) {
        return new Book(title, author, isbn, year);
    }

    public static Patron createPatron(String name, String email) {
        return new Patron(name, email);
    }
}
