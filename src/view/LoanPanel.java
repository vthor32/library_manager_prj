package view;

import model.Book;
import model.LibraryManager;
import model.Loan;
import model.Patron;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LoanPanel extends JPanel {
    private LibraryManager libraryManager;
    private DefaultTableModel loanTableModel;
    private JTable loanTable;
    private JComboBox<Book> bookComboBox;
    private JComboBox<Patron> patronComboBox;
    private JButton checkOutButton, checkInButton, clearButton;
    private JCheckBox showOpenLoansCheckBox;

    public LoanPanel(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Tabela de empréstimos
        String[] columnNames = {"Book", "Patron", "Loan Date", "Due Date", "Returned"};
        loanTableModel = new DefaultTableModel(columnNames, 0);
        loanTable = new JTable(loanTableModel);
        JScrollPane scrollPane = new JScrollPane(loanTable);
        add(scrollPane, BorderLayout.CENTER);

        // Formulário de entrada
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Book:"), gbc);

        gbc.gridx = 1;
        bookComboBox = new JComboBox<>();
        bookComboBox.setRenderer(new BookComboBoxRenderer());
        formPanel.add(bookComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Patron:"), gbc);

        gbc.gridx = 1;
        patronComboBox = new JComboBox<>();
        patronComboBox.setRenderer(new PatronComboBoxRenderer());
        formPanel.add(patronComboBox, gbc);

        JPanel buttonPanel = new JPanel();
        checkOutButton = new JButton("Check Out Book");
        checkOutButton.addActionListener(e -> checkOutBook());
        buttonPanel.add(checkOutButton);

        checkInButton = new JButton("Check In Book");
        checkInButton.addActionListener(e -> checkInBook());
        buttonPanel.add(checkInButton);

        clearButton = new JButton("Clear Selection");
        clearButton.addActionListener(e -> clearSelection());
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // Checkbox para mostrar apenas empréstimos em aberto
        showOpenLoansCheckBox = new JCheckBox("Show only open loans");
        showOpenLoansCheckBox.addActionListener(e -> updateLoanTable());
        add(showOpenLoansCheckBox, BorderLayout.NORTH);

        updateComboBoxes();
        updateLoanTable();

        loanTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && loanTable.getSelectedRow() != -1) {
                int selectedRow = loanTable.getSelectedRow();
                bookComboBox.setSelectedItem(libraryManager.searchBooks((String) loanTableModel.getValueAt(selectedRow, 0)).get(0));
                patronComboBox.setSelectedItem(libraryManager.searchPatrons((String) loanTableModel.getValueAt(selectedRow, 1)).get(0));
            }
        });
    }

    public void updateLoanTable() {
        loanTableModel.setRowCount(0);
        List<Loan> loans = libraryManager.getLoans();

        if (showOpenLoansCheckBox.isSelected()) {
            loans = loans.stream().filter(loan -> !loan.isReturned()).collect(Collectors.toList());
        }

        for (Loan loan : loans) {
            Object[] rowData = {loan.getBook().getTitle(), loan.getPatron().getName(), loan.getLoanDate(), loan.getDueDate(), loan.isReturned()};
            loanTableModel.addRow(rowData);
        }
    }

    public void updateComboBoxes() {
        bookComboBox.removeAllItems();
        patronComboBox.removeAllItems();

        List<Book> books = libraryManager.searchBooks("");
        List<Loan> loans = libraryManager.getLoans();

        // Filtra livros disponíveis
        List<Book> availableBooks = books.stream()
                .filter(Book::isAvailable)
                .filter(book -> loans.stream().noneMatch(loan -> loan.getBook().equals(book) && !loan.isReturned()))
                .collect(Collectors.toList());

        for (Book book : availableBooks) {
            bookComboBox.addItem(book);
        }

        List<Patron> patrons = libraryManager.searchPatrons("");

        // Filtra patrons sem empréstimo ativo
        List<Patron> availablePatrons = patrons.stream()
                .filter(patron -> loans.stream().noneMatch(loan -> loan.getPatron().equals(patron) && !loan.isReturned()))
                .collect(Collectors.toList());

        for (Patron patron : availablePatrons) {
            patronComboBox.addItem(patron);
        }
    }

    private void checkOutBook() {
        Book book = (Book) bookComboBox.getSelectedItem();
        Patron patron = (Patron) patronComboBox.getSelectedItem();

        if (book != null && patron != null) {
            boolean hasActiveLoan = libraryManager.getLoans().stream()
                    .anyMatch(loan -> loan.getPatron().equals(patron) && !loan.isReturned());

            if (hasActiveLoan) {
                JOptionPane.showMessageDialog(this, "This patron already has an active loan.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Loan loan = new Loan(book, patron, LocalDate.now(), LocalDate.now().plusWeeks(2));
            libraryManager.addLoan(loan);  // Adiciona o empréstimo ao sistema
            book.setAvailable(false);  // Marca o livro como não disponível
            updateComboBoxes();
            updateLoanTable();
            saveData();  // Salva os dados após a alteração
        }
    }

    private void checkInBook() {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow != -1) {
            Loan loan = libraryManager.getLoans().get(selectedRow);
            libraryManager.checkInBook(loan.getBook());
            updateComboBoxes();
            updateLoanTable();
            saveData();  // Salva os dados após a alteração
        }
    }

    private void clearSelection() {
        loanTable.clearSelection();
        bookComboBox.setSelectedItem(null);
        patronComboBox.setSelectedItem(null);
    }

    private void saveData() {
        try {
            libraryManager.saveData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}