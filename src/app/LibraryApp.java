package app;

import model.AuthManager;
import model.LibraryManager;
import model.Role;
import model.User;
import view.BookPanel;
import view.LoanPanel;
import view.PatronPanel;
import view.SearchPanel;
import view.UserManagementPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LibraryApp extends JFrame {
    private LibraryManager libraryManager;
    private AuthManager authManager;
    private User loggedInUser;
    private BookPanel bookPanel;
    private PatronPanel patronPanel;
    private LoanPanel loanPanel;
    private UserManagementPanel userManagementPanel;
    private SearchPanel searchPanel;

    public LibraryApp() {
        setLookAndFeel();
        libraryManager = new LibraryManager();
        authManager = new AuthManager();
        initializeUI();
        loadData();
    }

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se Nimbus não estiver disponível, use o padrão.
        }
    }

    private void initializeUI() {
        setTitle("Library System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Autenticação
        if (!authenticateUser()) {
            System.exit(0);
        }

        // Configurando o layout principal
        setLayout(new BorderLayout());

        // Painel de navegação
        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new GridLayout(1, 5));

        JButton bookButton = new JButton("Books");
        JButton patronButton = new JButton("Patrons");
        JButton loanButton = new JButton("Loans");
        JButton searchButton = new JButton("Search");
        JButton userManagementButton = new JButton("User Management");

        navigationPanel.add(bookButton);
        navigationPanel.add(patronButton);
        navigationPanel.add(loanButton);
        navigationPanel.add(searchButton);

        // Adiciona o botão de gerenciamento de usuários apenas para administradores
        if (loggedInUser.getRole() == Role.ADMIN) {
            navigationPanel.add(userManagementButton);
        }

        add(navigationPanel, BorderLayout.NORTH);

        // Painéis de conteúdo
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());

        loanPanel = new LoanPanel(libraryManager);
        bookPanel = new BookPanel(libraryManager, loanPanel);
        patronPanel = new PatronPanel(libraryManager, loanPanel);
        userManagementPanel = new UserManagementPanel(authManager, loggedInUser);
        searchPanel = new SearchPanel(libraryManager);

        contentPanel.add(bookPanel, "Books");
        contentPanel.add(patronPanel, "Patrons");
        contentPanel.add(loanPanel, "Loans");
        contentPanel.add(userManagementPanel, "User Management");
        contentPanel.add(searchPanel, "Search");

        add(contentPanel, BorderLayout.CENTER);

        // Event listeners para navegação
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();

        bookButton.addActionListener(e -> cardLayout.show(contentPanel, "Books"));
        patronButton.addActionListener(e -> cardLayout.show(contentPanel, "Patrons"));
        loanButton.addActionListener(e -> cardLayout.show(contentPanel, "Loans"));
        searchButton.addActionListener(e -> cardLayout.show(contentPanel, "Search"));
        userManagementButton.addActionListener(e -> cardLayout.show(contentPanel, "User Management"));
    }

    private boolean authenticateUser() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo ou título
        JLabel titleLabel = new JLabel("Library System Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(60, 60, 60));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 20, 5);
        loginPanel.add(titleLabel, gbc);

        // Campos de entrada
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        loginPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        loginPanel.add(passwordField, gbc);

        // Botão de login
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 20, 5);
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(60, 120, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginPanel.add(loginButton, gbc);

        // Adiciona ação ao botão de login
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            loggedInUser = authManager.authenticate(username, password);
            if (loggedInUser != null) {
                JOptionPane.showMessageDialog(loginPanel, "Login successful", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(loginPanel).dispose();
            } else {
                JOptionPane.showMessageDialog(loginPanel, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Exibe o painel de login em uma janela separada
        JDialog dialog = new JDialog(this, "Login", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(loginPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return loggedInUser != null;
    }

    private void loadData() {
        try {
            libraryManager.loadData();
            bookPanel.updateBookTable();
            patronPanel.updatePatronTable();
            loanPanel.updateLoanTable();
            loanPanel.updateComboBoxes(); // Atualiza comboBoxes após carregar dados
            JOptionPane.showMessageDialog(this, "Data loaded successfully");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryApp app = new LibraryApp();
            app.setVisible(true);
        });
    }
}