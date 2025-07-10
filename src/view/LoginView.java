package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Import frame customer agar bisa dipanggil setelah login sukses
import view.CustomerFrame;

public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 10, 160, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 40, 160, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 80, 80, 25);
        panel.add(loginButton);

        // Action listener untuk tombol login
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    // Method untuk proses login sederhana
    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Contoh validasi sederhana, ganti dengan validasi sebenarnya
        if ("customer".equals(username) && "pass123".equals(password)) {
            // Panggil method redirect ke dashboard/customer
            redirectToDashboard("CUSTOMER");
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method untuk mengalihkan ke halaman dashboard sesuai role
    private void redirectToDashboard(String role) {
        switch (role) {
            case "CUSTOMER":
                // Membuka frame CustomerFrame
                CustomerFrame customerFrame = new CustomerFrame();
                customerFrame.setVisible(true);

                // Tutup frame login setelah pindah ke customer page
                this.dispose();
                break;

            // Jika ada role lain, bisa tambah di sini

            default:
                JOptionPane.showMessageDialog(this, "Role tidak dikenali", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    // Main method untuk menjalankan login view
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            }
        });
    }
}
