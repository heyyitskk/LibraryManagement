package org.example.library.app;

import org.example.library.models.Book;
import org.example.library.models.Patron;
import org.example.library.service.LibraryService;
import org.example.library.service.NotificationListener;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        LibraryService library = new LibraryService();

        // Add books
        Book b1 = library.addBook("Effective Java", "Joshua Bloch", "9780134685991", 2018);
        Book b2 = library.addBook("Design Patterns", "Erich Gamma", "9780201633610", 1994);

        // Add patrons
        Patron p1 = library.addPatron("Alice", "alice@example.com");
        Patron p2 = library.addPatron("Bob", "bob@example.com");

        // Search
        System.out.println("Search by title 'Clean':");
        library.searchByTitle("Clean").forEach(System.out::println);

        System.out.println("Search by author 'Joshua':");
        library.searchByAuthor("Joshua").forEach(System.out::println);

        // Checkout by Alice
        System.out.println("Alice attempts checkout of Effective Java...");
        var loanOpt = library.checkout(b1.getIsbn(), p1.getId().toString());
        System.out.println(loanOpt.isPresent() ? "Checkout successful." : "Checkout failed.");

        // Bob attempts checkout of same book (should fail)
        System.out.println("Bob attempts checkout of same book...");
        var loanOpt2 = library.checkout(b1.getIsbn(), p2.getId().toString());
        System.out.println(loanOpt2.isPresent() ? "Checkout successful." : "Checkout failed (expected).");

        // Register listener for Bob so he gets notified when reserved book becomes
        // available
        library.registerReservationListener(p2.getId().toString(), new NotificationListener() {
            @Override
            public void onBookAvailable(Book book, String patronId) {
                System.out.println("Notification: patron=" + patronId + " book available=" + book.getTitle());
                // attempt to automatically checkout for the patron when notified
                var l = library.checkout(book.getIsbn(), patronId);
                System.out.println(l.isPresent() ? "Auto-checkout successful for " + patronId
                        : "Auto-checkout failed for " + patronId);
            }
        });

        // Bob reserves the book
        System.out.println("Bob places a reservation on Effective Java...");
        library.reserve(b1.getIsbn(), p2.getId().toString());

        // Alice returns the book — should trigger notification and auto-checkout for
        // Bob
        System.out.println("Alice returns Effective Java...");
        library.returnBook(b1.getIsbn(), p1.getId().toString());

        // Show recommendations for Bob
        System.out.println("Recommendations for Bob:");
        List<?> recs = library.recommend(p2.getId().toString(), 5);
        recs.forEach(System.out::println);
    }
}
