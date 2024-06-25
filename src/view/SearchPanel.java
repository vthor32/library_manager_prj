package view;

import model.Book;
import model.Loan;
import model.Patron;
import model.LibraryManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchPanel extends JPanel {
    private LibraryManager libraryManager;
    private JTextField bookSearchField;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField patronSearchField;
    private JTable patronTable;
    private DefaultTableModel patronTableModel;

    public SearchPanel(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        initializeUI();
        searchBooks();  // List all books by default
        searchPatrons();  // List all patrons by default
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Painel de busca de livros
        JPanel bookSearchPanel = new JPanel(new BorderLayout());
        JPanel bookSearchForm = new JPanel(new FlowLayout());
        bookSearchField = new JTextField(20);
        JButton bookSearchButton = new JButton("Search Books");
        bookSearchButton.addActionListener(e -> searchBooks());
        bookSearchForm.add(new JLabel("Search:"));
        bookSearchForm.add(bookSearchField);
        bookSearchForm.add(bookSearchButton);
        bookSearchPanel.add(bookSearchForm, BorderLayout.NORTH);

        String[] bookColumnNames = {"Title", "Author", "ISBN", "Category", "Loan Status"};
        bookTableModel = new DefaultTableModel(bookColumnNames, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        bookSearchPanel.add(bookScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Search Books", bookSearchPanel);

        // Painel de busca de patrons
        JPanel patronSearchPanel = new JPanel(new BorderLayout());
        JPanel patronSearchForm = new JPanel(new FlowLayout());
        patronSearchField = new JTextField(20);
        JButton patronSearchButton = new JButton("Search Patrons");
        patronSearchButton.addActionListener(e -> searchPatrons());
        patronSearchForm.add(new JLabel("Search:"));
        patronSearchForm.add(patronSearchField);
        patronSearchForm.add(patronSearchButton);
        patronSearchPanel.add(patronSearchForm, BorderLayout.NORTH);

        String[] patronColumnNames = {"Name", "Contact Info", "Loan Status"};
        patronTableModel = new DefaultTableModel(patronColumnNames, 0);
        patronTable = new JTable(patronTableModel);
        JScrollPane patronScrollPane = new JScrollPane(patronTable);
        patronSearchPanel.add(patronScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Search Patrons", patronSearchPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void searchBooks() {
        bookTableModel.setRowCount(0);
        String keyword = bookSearchField.getText();
        List<Book> books = libraryManager.searchBooks(keyword);
        List<Loan> loans = libraryManager.getLoans();

        for (Book book : books) {
            boolean isLoaned = loans.stream().anyMatch(loan -> loan.getBook().equals(book) && !loan.isReturned());
            Object[] rowData = {
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getCategory(),
                    isLoaned ? "Loaned" : "Available"
            };
            bookTableModel.addRow(rowData);
        }
    }

    private void searchPatrons() {
        patronTableModel.setRowCount(0);
        String keyword = patronSearchField.getText();
        List<Patron> patrons = libraryManager.searchPatrons(keyword);
        List<Loan> loans = libraryManager.getLoans();

        for (Patron patron : patrons) {
            boolean hasOpenLoan = loans.stream().anyMatch(loan -> loan.getPatron().equals(patron) && !loan.isReturned());
            Object[] rowData = {
                    patron.getName(),
                    patron.getContactInfo(),
                    hasOpenLoan ? "Has Open Loan" : "No Open Loans"
            };
            patronTableModel.addRow(rowData);
        }
    }
}