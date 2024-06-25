package model;

import java.io.Serializable;
import java.util.List;

public class LibraryData implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Book> books;
    private List<Patron> patrons;
    private List<Loan> loans;

    public LibraryData(List<Book> books, List<Patron> patrons, List<Loan> loans) {
        this.books = books;
        this.patrons = patrons;
        this.loans = loans;
    }

    // Getters e Setters
    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }

    public List<Patron> getPatrons() { return patrons; }
    public void setPatrons(List<Patron> patrons) { this.patrons = patrons; }

    public List<Loan> getLoans() { return loans; }
    public void setLoans(List<Loan> loans) { this.loans = loans; }
}
