# Library Management System (Java)

This project is a simple in-memory Library Management System demonstrating OOP, SOLID principles, and design patterns.

Features implemented:
- Book management (add, remove, search)
- Patron management (add, borrowing history)
- Lending (checkout, return)
- Reservation system (Observer-like notification)
- Basic recommendation engine (based on borrow history)

Run tests and build with Maven:

```bash
mvn test
```

Class diagram (Mermaid):

```mermaid
classDiagram
    Book <|-- Loan
    Patron "1" --* "many" Loan : borrows
    LibraryService o-- BookRepository
    LibraryService o-- PatronRepository
    LibraryService o-- LoanRepository
    LibraryService o-- LendingService
    LendingService o-- ReservationService
    RecommendationService o-- BookRepository
```

See `src/main/java/org/example/library` for source code.
