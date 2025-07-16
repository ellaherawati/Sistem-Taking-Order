package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import view.KasirView;

public class KasirFrame extends JFrame {
    private KasirView kasirView;
    private Map<String, String> orderData;
    private boolean isPaid = false;

    public KasirFrame() {
        setTitle("Dashboard Kasir");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        kasirView = new KasirView();
        add(kasirView);

        initOrderData();
        setupEvents();
    }

    private void initOrderData() {
        orderData = new HashMap<>();
        orderData.put("NOTA123", "Order ID: NOTA123\nItem: Nasi Goreng, 2 porsi\nTotal: Rp 40.000");
        orderData.put("NOTA456", "Order ID: NOTA456\nItem: Mie Ayam, 1 porsi\nTotal: Rp 15.000");
    }

    private void setupEvents() {
        kasirView.getButtonCari().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String notaId = kasirView.getTextFieldNotaId().getText().trim();
                if (notaId.isEmpty()) {
                    JOptionPane.showMessageDialog(KasirFrame.this, "ID Nota tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String orderInfo = orderData.get(notaId);
                if (orderInfo == null) {
                    kasirView.getTextAreaOrder().setText("");
                    kasirView.getButtonBayar().setEnabled(false);
                    isPaid = false;
                    JOptionPane.showMessageDialog(KasirFrame.this, "Order dengan ID Nota tersebut tidak ditemukan", "Not Found", JOptionPane.WARNING_MESSAGE);
                } else {
                    kasirView.getTextAreaOrder().setText(orderInfo);
                    kasirView.getButtonBayar().setEnabled(true);
                    isPaid = false;
                }
            }
        });

        kasirView.getButtonBayar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPaid) {
                    JOptionPane.showMessageDialog(KasirFrame.this, "Pembayaran sudah dilakukan.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(KasirFrame.this, "Apakah pembayaran sudah diterima?", "Konfirmasi Pembayaran", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    isPaid = true;
                    JOptionPane.showMessageDialog(KasirFrame.this, "Pembayaran berhasil dan terverifikasi.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    kasirView.getButtonBayar().setEnabled(false);
                    kasirView.getTextAreaOrder().append("\n\nStatus: Lunas");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new KasirFrame().setVisible(true);
        });
    }
}
