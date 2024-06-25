package view;

import model.AuthManager;
import model.Role;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private AuthManager authManager;
    private User loggedInUser;
    private DefaultTableModel userTableModel;
    private JTable userTable;

    public UserManagementPanel(AuthManager authManager, User loggedInUser) {
        this.authManager = authManager;
        this.loggedInUser = loggedInUser;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Aba de listagem de usuários
        JPanel listUsersPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Username", "Role"};
        userTableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(userTableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        listUsersPanel.add(scrollPane, BorderLayout.CENTER);

        JButton deleteUserButton = new JButton("Delete User");
        deleteUserButton.addActionListener(e -> deleteUser());
        listUsersPanel.add(deleteUserButton, BorderLayout.SOUTH);

        tabbedPane.addTab("List Users", listUsersPanel);

        // Aba de adicionar usuário
        JPanel addUserPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<Role> roleComboBox = new JComboBox<>(Role.values());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        addUserPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        addUserPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        addUserPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        addUserPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        addUserPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        addUserPanel.add(roleComboBox, gbc);

        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            Role role = (Role) roleComboBox.getSelectedItem();

            User newUser = new User(username, password, role);
            boolean success = authManager.addUser(newUser);
            if (success) {
                updateUserTable();
                saveData();  // Salva os dados após a alteração
                JOptionPane.showMessageDialog(this, "User added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        addUserPanel.add(addUserButton, gbc);

        tabbedPane.addTab("Add User", addUserPanel);

        add(tabbedPane, BorderLayout.CENTER);

        updateUserTable();
    }

    private void updateUserTable() {
        userTableModel.setRowCount(0);
        List<User> users = authManager.getUsers();

        for (User user : users) {
            Object[] rowData = {user.getUsername(), user.getRole()};
            userTableModel.addRow(rowData);
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            String username = (String) userTableModel.getValueAt(selectedRow, 0);
            if (!username.equals(loggedInUser.getUsername())) {
                authManager.deleteUser(username);
                updateUserTable();
                saveData();  // Salva os dados após a alteração
            } else {
                JOptionPane.showMessageDialog(this, "You cannot delete your own account", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveData() {
        authManager.saveUsers();
    }
}