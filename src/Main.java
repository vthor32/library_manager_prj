import model.Book;
import model.LibraryManager;
import model.Loan;
import model.Patron;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        LibraryManager libraryManager = new LibraryManager();

        // Adicionar livros
        Book book1 = new Book("Java Programming", "John Doe", "1234567890", "Programming");
        Book book2 = new Book("Effective Java", "Joshua Bloch", "0987654321", "Programming");
        libraryManager.addBook(book1);
        libraryManager.addBook(book2);

        // Adicionar patronos
        Patron patron1 = new Patron("Alice", "alice@example.com");
        Patron patron2 = new Patron("Bob", "bob@example.com");
        libraryManager.addPatron(patron1);
        libraryManager.addPatron(patron2);

        // Buscar livros
        List<Book> foundBooks = libraryManager.searchBooks("Java");
        System.out.println("Found Books: " + foundBooks);

        // Realizar empréstimo
        libraryManager.checkOutBook(book1, patron1);
        System.out.println("model.Book checked out: " + book1);

        // Empréstimos em atraso
        List<Loan> overdueLoans = libraryManager.getLoans();
        System.out.println("Overdue Loans: " + overdueLoans);

        // Devolver livro
        libraryManager.checkInBook(book1);
        System.out.println("model.Book checked in: " + book1);
    }
}
