package view;

import controller.AuthController;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton customerButton;

    private AuthController authController;

    public LoginView() {
        authController = new AuthController(); // pastikan AuthController sudah siap
        
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login");
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 10, 200, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 45, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 45, 200, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 80, 90, 25);
        panel.add(loginButton);

        customerButton = new JButton("Customer");
        customerButton.setBounds(210, 80, 90, 25);
        panel.add(customerButton);

        // Action listener untuk login
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Action listener untuk tombol customer
        customerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCustomerPage();
            }
        });
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Pakai AuthController untuk validasi login
        String loggedInUser = authController.login(username, password);

        if (loggedInUser != null) {
            // Login sukses, arahkan ke dashboard sesuai role user
            redirectToDashboard(loggedInUser.getRole());
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah", "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openCustomerPage() {
        CustomerFrame customerFrame = new CustomerFrame();
        customerFrame.setVisible(true);
        this.dispose(); // tutup halaman login
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
