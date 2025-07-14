package view;

import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.*;

public class CustomerView extends JPanel {

    private JComboBox<Order.PaymentMethod> paymentComboBox;
    private JButton checkoutButton;

    private static final Color SUCCESS_COLOR = new Color(0, 153, 51);

    public CustomerView(AuthController authController) {
        setLayout(new BorderLayout());

        // Contoh isi comboBox metode pembayaran
        paymentComboBox = new JComboBox<>(Order.PaymentMethod.values());

        // Tombol Checkout
        checkoutButton = createStyledButton("Checkout", SUCCESS_COLOR, "🛍️");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Pilih Metode Pembayaran:"));
        topPanel.add(paymentComboBox);
        topPanel.add(checkoutButton);

        add(topPanel, BorderLayout.NORTH);

        setupEventHandlers();
    }

    private JButton createStyledButton(String text, Color bgColor, String iconEmoji) {
        JButton btn = new JButton(iconEmoji + " " + text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void setupEventHandlers() {
        checkoutButton.addActionListener(e -> {
            Order.PaymentMethod selectedPayment = (Order.PaymentMethod) paymentComboBox.getSelectedItem();

            if (selectedPayment == null) {
                JOptionPane.showMessageDialog(this, "Pilih metode pembayaran terlebih dahulu.");
                return;
            }

            if (selectedPayment == Order.PaymentMethod.QRIS) {
                boolean paymentSuccess = doPaymentQRIS();

                if (paymentSuccess) {
                    String notaDetail = generateNotaString();
                    NotaFrame notaFrame = new NotaFrame(notaDetail);
                    notaFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Pembayaran gagal. Silakan coba lagi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Metode pembayaran selain QRIS belum didukung.");
            }
        });

        checkoutButton.addActionListener(e -> {
            Order.PaymentMethod selectedPayment = (Order.PaymentMethod) paymentComboBox.getSelectedItem();
        
            if (selectedPayment == null) {
                JOptionPane.showMessageDialog(this, "Pilih metode pembayaran terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            if (selectedPayment == Order.PaymentMethod.QRIS) {
                boolean paymentSuccess = false;
                try {
                    // Simulasi proses pembayaran QRIS (ganti dengan implementasi asli)
                    paymentSuccess = doPaymentQRIS();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat proses pembayaran: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                if (paymentSuccess) {
                    String notaDetail = generateNotaString();
                    NotaFrame notaFrame = new NotaFrame(notaDetail);
                    notaFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Pembayaran gagal. Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Metode pembayaran selain QRIS belum didukung.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
    }

    // Simulasi proses pembayaran QRIS (ganti dengan implementasi asli)
    private boolean doPaymentQRIS() {
        // Contoh simulasi pembayaran berhasil
        return true;
    }

    // Fungsi membuat string nota secara dinamis (ganti dengan data nyata)
    private String generateNotaString() {
        return "Terima kasih sudah berbelanja!\n\n"
            + "Total: Rp 100.000\n"
            + "Metode Pembayaran: QRIS\n"
            + "Tanggal: " + LocalDateTime.now().toString()
            + "\n\nItem:\n"
            + "1x Nasi Goreng - Rp 30.000\n"
            + "2x Es Teh - Rp 20.000\n";
    }

    // Class NotaFrame menampilkan nota pembayaran
    public static class NotaFrame extends JFrame {
        public NotaFrame(String notaDetail) {
            setTitle("Nota Pembayaran");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea textArea = new JTextArea(notaDetail);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            add(new JScrollPane(textArea), BorderLayout.CENTER);
        }
    }

    // Enum contoh Order.PaymentMethod, jika Anda sudah punya di tempat lain, bisa pakai yang asli.
    public static class Order {
        public enum PaymentMethod {
            CASH,  QRIS
        }
    }

    // Placeholder AuthController (ganti dengan implementasi nyata)
    public static class AuthController {}
}
