package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginFrame() {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize components in EDT
        SwingUtilities.invokeLater(() -> {
            initializeComponents();
            setupLayout();
            setupEventHandlers();
        });
    }

    
    
    private void initializeComponents() {
        setTitle("Sistem Manajemen Restoran - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set window background
        getContentPane().setBackground(new Color(245, 245, 245));
        
        // Initialize components
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("LOGIN");
        
        // Set fonts - use fallback fonts for better compatibility
        Font inputFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        
        usernameField.setFont(inputFont);
        passwordField.setFont(inputFont);
        loginButton.setFont(buttonFont);
        
        // Enhanced field styling
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Set explicit sizes for better compatibility
        Dimension fieldSize = new Dimension(300, 45);
        usernameField.setPreferredSize(fieldSize);
        usernameField.setMinimumSize(fieldSize);
        usernameField.setMaximumSize(fieldSize);
        
        passwordField.setPreferredSize(fieldSize);
        passwordField.setMinimumSize(fieldSize);
        passwordField.setMaximumSize(fieldSize);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        // Logo/Icon - use simple text instead of emoji for better compatibility
        JLabel logoLabel = new JLabel("🍽️");
        logoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 48));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("SISTEM MANAJEMEN RESTORAN");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Silakan masuk untuk melanjutkan");
        subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        // Username Section
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        usernameLabel.setForeground(new Color(52, 73, 94));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Password Section
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        passwordLabel.setForeground(new Color(52, 73, 94));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login Button styling
        Dimension buttonSize = new Dimension(300, 50);
        loginButton.setPreferredSize(buttonSize);
        loginButton.setMinimumSize(buttonSize);
        loginButton.setMaximumSize(buttonSize);
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set cursor
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (loginButton.isEnabled()) {
                    loginButton.setBackground(new Color(41, 128, 185));
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (loginButton.isEnabled()) {
                    loginButton.setBackground(new Color(52, 152, 219));
                }
            }
        });
        
        // Create wrapper panel for button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        
        // Arrange form components
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(createCenteredPanel(usernameField));
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(createCenteredPanel(passwordField));
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(buttonPanel);
        
        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setBackground(new Color(236, 240, 241));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Add some info text
        JLabel infoLabel = new JLabel("Gunakan akun yang telah terdaftar untuk masuk", JLabel.CENTER);
        infoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        infoLabel.setForeground(new Color(127, 140, 141));
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        
        // Assemble main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createCenteredPanel(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);
        panel.add(component);
        return panel;
    }
    
    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(e -> performLogin());
        
        // Enter key support
        ActionListener loginAction = e -> performLogin();
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        
        // Focus listeners for better UX
        usernameField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameField.selectAll();
            }
        });
        
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordField.selectAll();
            }
        });
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            showErrorDialog("Username dan password harus diisi!");
            return;
        }
        
        // Disable button during login
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
        
        // Simulate login process (replace with actual AuthController logic)
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    // Simulate authentication delay
                    Thread.sleep(1000);
                    
                    // TODO: Replace with actual authentication logic
                    // return authController.login(username, password);
                    
                    // Temporary mock authentication
                    return authenticateUser(username, password);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    
                    if (success) {
                        // TODO: Replace with actual user retrieval
                        // User currentUser = authController.getCurrentUser();
                        String userRole = getUserRole(username);
                        
                        showSuccessDialog("Login berhasil!\nSelamat datang, " + username);
                        openDashboard(userRole);
                        dispose();
                    } else {
                        showErrorDialog("Username atau password salah!");
                        passwordField.setText("");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    showErrorDialog("Terjadi kesalahan saat login: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    // Re-enable button
                    loginButton.setEnabled(true);
                    loginButton.setText("LOGIN");
                }
            }
        };
        
        worker.execute();
    }
    
    // Temporary mock authentication method
    private boolean authenticateUser(String username, String password) {
        // TODO: Replace with actual authentication logic
        // For now, accept any non-empty credentials
        return !username.isEmpty() && !password.isEmpty();
    }
    
    // Temporary method to get user role
    private String getUserRole(String username) {
        // TODO: Replace with actual role retrieval logic
        // Mock roles based on username
        if (username.equalsIgnoreCase("admin")) {
            return "ADMIN";
        } else if (username.equalsIgnoreCase("kasir")) {
            return "KASIR";
        } else if (username.equalsIgnoreCase("manager")) {
            return "MANAGER";
        } else {
            return "CUSTOMER";
        }
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Update method openDashboard di LoginFrame.java

private void openDashboard(String role) {
    SwingUtilities.invokeLater(() -> {
        try {
            switch (role) {
                case "ADMIN":
                    // Uncomment ketika AdminFrame sudah siap
                    // new AdminFrame().setVisible(true);
                    JOptionPane.showMessageDialog(this, "Admin Dashboard akan terbuka", "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "KASIR":
                    // Uncomment ketika KasirFrame sudah siap
                    // new KasirFrame().setVisible(true);
                    JOptionPane.showMessageDialog(this, "Kasir Dashboard akan terbuka", "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "MANAGER":
                    // Uncomment ketika ManagerFrame sudah siap
                    // new ManagerFrame().setVisible(true);
                    JOptionPane.showMessageDialog(this, "Manager Dashboard akan terbuka", "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "CUSTOMER":
                    // Buka CustomerFrame (Dapur Arunika)
                    new CustomerFrame().setVisible(true);
                    break;
                default:
                    showErrorDialog("Role tidak dikenal: " + role);
                    break;
            }
        } catch (Exception e) {
            showErrorDialog("Terjadi kesalahan saat membuka dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    });
}
    
    // Main method for testing
    public static void main(String[] args) {
        // Set system properties for better integration
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Restaurant Management System");
        
        SwingUtilities.invokeLater(() -> {
            try {
                String osName = System.getProperty("os.name").toLowerCase();
                if (osName.contains("windows")) {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } else if (osName.contains("mac")) {
                    UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
                } else {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                }
            } catch (Exception e) {
                // Fallback to cross-platform look and feel
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            }
            
            new LoginFrame().setVisible(true);
        });
    }
}