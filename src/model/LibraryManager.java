package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.LocalDateAdapter;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryManager {
    private List<Book> books;
    private List<Patron> patrons;
    private List<Loan> loans;
    private Gson gson;

    public LibraryManager() {
        books = new ArrayList<>();
        patrons = new ArrayList<>();
        loans = new ArrayList<>();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    // Gerenciamento de Livros
    public void addBook(Book book) {
        books.add(book);
    }

    public void editBook(Book oldBook, Book newBook) {
        int index = books.indexOf(oldBook);
        if (index != -1) {
            books.set(index, newBook);
        }
    }

    public void deleteBook(Book book) {
        books.remove(book);
    }

    public List<Book> searchBooks(String keyword) {
        return books.stream()
                .filter(book -> book.getTitle().contains(keyword) || book.getAuthor().contains(keyword) || book.getIsbn().contains(keyword) || book.getCategory().contains(keyword))
                .collect(Collectors.toList());
    }

    // Gerenciamento de Patronos
    public void addPatron(Patron patron) {
        patrons.add(patron);
    }

    public void editPatron(Patron oldPatron, Patron newPatron) {
        int index = patrons.indexOf(oldPatron);
        if (index != -1) {
            patrons.set(index, newPatron);
        }
    }

    public void deletePatron(Patron patron) {
        patrons.remove(patron);
    }

    public List<Patron> searchPatrons(String keyword) {
        return patrons.stream()
                .filter(patron -> patron.getName().contains(keyword) || patron.getContactInfo().contains(keyword))
                .collect(Collectors.toList());
    }

    // Gerenciamento de Empréstimos
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void checkOutBook(Book book, Patron patron) {
        if (book.isAvailable()) {
            book.setAvailable(false);
            Loan loan = new Loan(book, patron, LocalDate.now(), LocalDate.now().plusWeeks(2));
            loans.add(loan);
        }
    }

    public void checkInBook(Book book) {
        for (Loan loan : loans) {
            if (loan.getBook().equals(book) && !loan.isReturned()) {
                loan.setReturned(true);
                book.setAvailable(true);
                break;
            }
        }
    }

    public List<Loan> getLoans() {
        return loans;
    }

    // Métodos de I/O com JSON
    public void saveData() throws IOException {
        LibraryData data = new LibraryData(books, patrons, loans);
        try (Writer writer = new FileWriter("library_data.json")) {
            gson.toJson(data, writer);
        }
    }

    public void loadData() throws IOException {
        try (Reader reader = new FileReader("library_data.json")) {
            LibraryData data = gson.fromJson(reader, LibraryData.class);
            books = data.getBooks();
            patrons = data.getPatrons();
            loans = data.getLoans();
        }
    }
}