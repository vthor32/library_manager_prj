package model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;

public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("book")
    private Book book;

    @SerializedName("patron")
    private Patron patron;

    @SerializedName("loanDate")
    private LocalDate loanDate;

    @SerializedName("dueDate")
    private LocalDate dueDate;

    @SerializedName("isReturned")
    private boolean isReturned;

    public Loan(Book book, Patron patron, LocalDate loanDate, LocalDate dueDate) {
        this.book = book;
        this.patron = patron;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.isReturned = false;
    }

    // Getters e Setters
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public Patron getPatron() { return patron; }
    public void setPatron(Patron patron) { this.patron = patron; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isReturned() { return isReturned; }
    public void setReturned(boolean isReturned) { this.isReturned = isReturned; }

    @Override
    public String toString() {
        return "model.Loan [model.Book=" + book + ", model.Patron=" + patron + ", model.Loan Date=" + loanDate + ", Due Date=" + dueDate + ", Returned=" + isReturned + "]";
    }
}
