package view;

import model.Book;
import model.LibraryManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class BookPanel extends JPanel {
    private LibraryManager libraryManager;
    private DefaultTableModel bookTableModel;
    private JTable bookTable;
    private LoanPanel loanPanel;

    public BookPanel(LibraryManager libraryManager, LoanPanel loanPanel) {
        this.libraryManager = libraryManager;
        this.loanPanel = loanPanel;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Tabela de livros
        String[] columnNames = {"Title", "Author", "ISBN", "Category", "Available"};
        bookTableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // Formulário de entrada
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField isbnField = new JTextField(20);
        JTextField categoryField = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(categoryField, gbc);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();
            String category = categoryField.getText();

            Book book = new Book(title, author, isbn, category);
            libraryManager.addBook(book);
            updateBookTable();
            loanPanel.updateComboBoxes();
            saveData();  // Salva os dados após a alteração
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        formPanel.add(addButton, gbc);

        JButton deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(e -> deleteBook());

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        formPanel.add(deleteButton, gbc);

        add(formPanel, BorderLayout.SOUTH);

        updateBookTable();
    }

    public void updateBookTable() {
        bookTableModel.setRowCount(0);
        List<Book> books = libraryManager.searchBooks("");

        for (Book book : books) {
            Object[] rowData = {
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getCategory(),
                    book.isAvailable() ? "Yes" : "No"
            };
            bookTableModel.addRow(rowData);
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            String isbn = (String) bookTableModel.getValueAt(selectedRow, 2);
            Book bookToDelete = null;
            List<Book> books = libraryManager.searchBooks("");
            for (Book book : books) {
                if (book.getIsbn().equals(isbn)) {
                    bookToDelete = book;
                    break;
                }
            }
            if (bookToDelete != null) {
                libraryManager.deleteBook(bookToDelete);
                updateBookTable();
                loanPanel.updateComboBoxes();
                saveData();  // Salva os dados após a alteração
            }
        }
    }

    private void saveData() {
        try {
            libraryManager.saveData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}