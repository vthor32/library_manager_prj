package view;

import model.LibraryManager;
import model.Patron;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PatronPanel extends JPanel {
    private LibraryManager libraryManager;
    private DefaultTableModel patronTableModel;
    private JTable patronTable;
    private LoanPanel loanPanel;

    public PatronPanel(LibraryManager libraryManager, LoanPanel loanPanel) {
        this.libraryManager = libraryManager;
        this.loanPanel = loanPanel;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Tabela de patrons
        String[] columnNames = {"Name", "Contact Info"};
        patronTableModel = new DefaultTableModel(columnNames, 0);
        patronTable = new JTable(patronTableModel);
        JScrollPane scrollPane = new JScrollPane(patronTable);
        add(scrollPane, BorderLayout.CENTER);

        // Formulário de entrada
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField contactInfoField = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Contact Info:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(contactInfoField, gbc);

        JButton addButton = new JButton("Add Patron");
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String contactInfo = contactInfoField.getText();

            Patron patron = new Patron(name, contactInfo);
            libraryManager.addPatron(patron);
            updatePatronTable();
            loanPanel.updateComboBoxes();
            saveData();  // Salva os dados após a alteração
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        formPanel.add(addButton, gbc);

        JButton deleteButton = new JButton("Delete Patron");
        deleteButton.addActionListener(e -> deletePatron());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        formPanel.add(deleteButton, gbc);

        add(formPanel, BorderLayout.SOUTH);

        updatePatronTable();
    }

    public void updatePatronTable() {
        patronTableModel.setRowCount(0);
        List<Patron> patrons = libraryManager.searchPatrons("");

        for (Patron patron : patrons) {
            Object[] rowData = {
                    patron.getName(),
                    patron.getContactInfo()
            };
            patronTableModel.addRow(rowData);
        }
    }

    private void deletePatron() {
        int selectedRow = patronTable.getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) patronTableModel.getValueAt(selectedRow, 0);
            Patron patron = libraryManager.searchPatrons(name).get(0);
            libraryManager.deletePatron(patron);
            updatePatronTable();
            loanPanel.updateComboBoxes();
            saveData();  // Salva os dados após a alteração
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