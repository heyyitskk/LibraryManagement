package org.example.library.models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Loan {
    private final UUID id;
    private final UUID bookId;
    private final UUID patronId;
    private final LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(UUID bookId, UUID patronId, LocalDate checkoutDate, LocalDate dueDate) {
        this.id = UUID.randomUUID();
        this.bookId = bookId;
        this.patronId = patronId;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
    }

    public UUID getId() {
        return id;
    }

    public UUID getBookId() {
        return bookId;
    }

    public UUID getPatronId() {
        return patronId;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
