package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class PaymentMethodDialog extends JDialog {
    private String selectedPaymentMethod = null;
    private boolean paymentConfirmed = false;
    private int totalAmount;
    
    public PaymentMethodDialog(JFrame parent, int totalAmount) {
        super(parent, "Metode Pembayaran", true);
        this.totalAmount = totalAmount;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        getContentPane().setBackground(new Color(248, 249, 250));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Payment Methods Panel
        JPanel paymentPanel = createPaymentMethodsPanel();
        add(paymentPanel, BorderLayout.CENTER);
        
        // Bottom Panel
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Metode Pembayaran");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel totalLabel = new JLabel("Total: Rp. " + String.format("%,d", totalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(new Color(70, 130, 180));
        headerContent.add(titleLabel, BorderLayout.CENTER);
        headerContent.add(totalLabel, BorderLayout.SOUTH);
        
        headerPanel.add(headerContent, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createPaymentMethodsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 249, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel instructionLabel = new JLabel("Pilih metode pembayaran yang diinginkan:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setForeground(new Color(60, 60, 60));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Payment methods grid
        JPanel paymentGrid = new JPanel(new GridLayout(1, 2, 30, 0));
        paymentGrid.setBackground(new Color(248, 249, 250));
        paymentGrid.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // QRIS Payment
        JPanel qrisPanel = createPaymentMethodPanel(
            "QRIS", 
            "💳", 
            "Scan QR Code untuk pembayaran digital",
            new Color(0, 123, 255)
        );
        
        // Cash Payment
        JPanel cashPanel = createPaymentMethodPanel(
            "CASH", 
            "💵", 
            "Pembayaran tunai di tempat",
            new Color(40, 167, 69)
        );
        
        paymentGrid.add(qrisPanel);
        paymentGrid.add(cashPanel);
        
        mainPanel.add(instructionLabel, BorderLayout.NORTH);
        mainPanel.add(paymentGrid, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createPaymentMethodPanel(String methodName, String icon, String description, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
            BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Method name
        JLabel nameLabel = new JLabel(methodName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(accentColor);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Description
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descArea.setForeground(new Color(100, 100, 100));
        descArea.setBackground(Color.WHITE);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(descArea);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Add click handler
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectPaymentMethod(methodName, panel);
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    BorderFactory.createEmptyBorder(25, 20, 25, 20)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!methodName.equals(selectedPaymentMethod)) {
                    panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                        BorderFactory.createEmptyBorder(25, 20, 25, 20)
                    ));
                }
            }
        });
        
        return panel;
    }
    
    private void selectPaymentMethod(String methodName, JPanel panel) {
        selectedPaymentMethod = methodName;
        
        // Reset all panels
        Component[] components = ((JPanel) getContentPane().getComponent(1)).getComponents();
        if (components.length > 0) {
            JPanel paymentGrid = (JPanel) ((JPanel) components[0]).getComponent(1);
            for (Component comp : paymentGrid.getComponents()) {
                if (comp instanceof JPanel) {
                    if (comp instanceof JPanel) {
                        ((JPanel) comp).setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                            BorderFactory.createEmptyBorder(25, 20, 25, 20)
                        ));
                    }
                }
            }
        }
        
        // Highlight selected panel
        Color accentColor = methodName.equals("QRIS") ? new Color(0, 123, 255) : new Color(40, 167, 69);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 3),
            BorderFactory.createEmptyBorder(24, 19, 24, 19)
        ));
        
        // Show payment details
        if (methodName.equals("QRIS")) {
            showQRISDetails();
        } else {
            showCashDetails();
        }
    }
    
    private void showQRISDetails() {
        JDialog qrisDialog = new JDialog(this, "QRIS Payment", true);
        qrisDialog.setSize(400, 450);
        qrisDialog.setLocationRelativeTo(this);
        qrisDialog.setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel headerLabel = new JLabel("Scan QR Code");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        // QR Code area
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBackground(Color.WHITE);
        qrPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // QR Code clickable area
        JPanel qrClickablePanel = createClickableQRCode();
        
        JLabel totalLabel = new JLabel("Total: Rp. " + String.format("%,d", totalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel instructionLabel = new JLabel("Klik QR Code untuk memperbesar");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setForeground(new Color(100, 100, 100));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        qrPanel.add(qrClickablePanel, BorderLayout.CENTER);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(totalLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(instructionLabel);
        
        qrPanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton confirmButton = new JButton("Konfirmasi Pembayaran");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(0, 123, 255));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setPreferredSize(new Dimension(180, 35));
        
        JButton cancelButton = new JButton("Batal");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(80, 35));
        
        confirmButton.addActionListener(e -> {
            paymentConfirmed = true;
            qrisDialog.dispose();
            dispose();
        });
        
        cancelButton.addActionListener(e -> qrisDialog.dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        qrisDialog.add(headerPanel, BorderLayout.NORTH);
        qrisDialog.add(qrPanel, BorderLayout.CENTER);
        qrisDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        qrisDialog.setVisible(true);
    }
    
    private JPanel createClickableQRCode() {
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBackground(Color.WHITE);
        qrPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // QR Code placeholder
        JLabel qrLabel = new JLabel();
        qrLabel.setPreferredSize(new Dimension(200, 200));
        qrLabel.setBackground(new Color(240, 240, 240));
        qrLabel.setOpaque(true);
        qrLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qrLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Create QR pattern
        qrLabel.setText("<html><div style='text-align: center; font-size: 60px;'>⬜⬛⬜⬛⬜<br>⬛⬜⬛⬜⬛<br>⬜⬛⬜⬛⬜<br>⬛⬜⬛⬜⬛<br>⬜⬛⬜⬛⬜</div></html>");
        
        // Add click listener to QR code
        qrLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showLargeBarcode();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                qrLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 3));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                qrLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
            }
        });
        
        qrPanel.add(qrLabel, BorderLayout.CENTER);
        
        return qrPanel;
    }
    
    private void showLargeBarcode() {
        JDialog barcodeDialog = new JDialog(this, "Barcode", true);
        barcodeDialog.setSize(500, 600);
        barcodeDialog.setLocationRelativeTo(this);
        barcodeDialog.setLayout(new BorderLayout());
        barcodeDialog.setResizable(false);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel headerLabel = new JLabel("Barcode Payment");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        // Barcode area
        JPanel barcodePanel = new JPanel(new BorderLayout());
        barcodePanel.setBackground(Color.WHITE);
        barcodePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Large barcode display
        JLabel barcodeLabel = new JLabel();
        barcodeLabel.setPreferredSize(new Dimension(400, 120));
        barcodeLabel.setBackground(Color.WHITE);
        barcodeLabel.setOpaque(true);
        barcodeLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        barcodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        barcodeLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Create barcode pattern
        StringBuilder barcodePattern = new StringBuilder();
        barcodePattern.append("<html><div style='text-align: center; font-size: 16px; font-family: monospace;'>");
        barcodePattern.append("||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||<br>");
        barcodePattern.append("||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||<br>");
        barcodePattern.append("||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||<br>");
        barcodePattern.append("||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||&nbsp;|&nbsp;|&nbsp;||&nbsp;|&nbsp;||<br>");
        barcodePattern.append("</div></html>");
        
        barcodeLabel.setText(barcodePattern.toString());
        
        // Barcode number
        JLabel barcodeNumber = new JLabel("1234567890123");
        barcodeNumber.setFont(new Font("Arial", Font.BOLD, 18));
        barcodeNumber.setHorizontalAlignment(SwingConstants.CENTER);
        barcodeNumber.setForeground(new Color(60, 60, 60));
        
        // Payment info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel totalLabel = new JLabel("Total: Rp. " + String.format("%,d", totalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(new Color(0, 123, 255));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel("Tunjukkan barcode ini kepada kasir");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionLabel.setForeground(new Color(100, 100, 100));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel instruction2Label = new JLabel("atau scan dengan aplikasi pembayaran");
        instruction2Label.setFont(new Font("Arial", Font.PLAIN, 14));
        instruction2Label.setForeground(new Color(100, 100, 100));
        instruction2Label.setHorizontalAlignment(SwingConstants.CENTER);
        instruction2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(totalLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(instructionLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(instruction2Label);
        
        // Main barcode content
        JPanel barcodeContent = new JPanel();
        barcodeContent.setLayout(new BoxLayout(barcodeContent, BoxLayout.Y_AXIS));
        barcodeContent.setBackground(Color.WHITE);
        
        barcodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        barcodeNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        barcodeContent.add(barcodeLabel);
        barcodeContent.add(Box.createVerticalStrut(10));
        barcodeContent.add(barcodeNumber);
        barcodeContent.add(Box.createVerticalStrut(30));
        barcodeContent.add(infoPanel);
        
        barcodePanel.add(barcodeContent, BorderLayout.CENTER);
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton closeButton = new JButton("Tutup");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(108, 117, 125));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> barcodeDialog.dispose());
        
        buttonPanel.add(closeButton);
        
        barcodeDialog.add(headerPanel, BorderLayout.NORTH);
        barcodeDialog.add(barcodePanel, BorderLayout.CENTER);
        barcodeDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        barcodeDialog.setVisible(true);
    }
    
    private void showCashDetails() {
        JDialog cashDialog = new JDialog(this, "Cash Payment", true);
        cashDialog.setSize(400, 300);
        cashDialog.setLocationRelativeTo(this);
        cashDialog.setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 167, 69));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel headerLabel = new JLabel("Pembayaran Tunai");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel cashIcon = new JLabel("💵");
        cashIcon.setFont(new Font("Arial", Font.PLAIN, 48));
        cashIcon.setHorizontalAlignment(SwingConstants.CENTER);
        cashIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel totalLabel = new JLabel("Total yang harus dibayar:");
        totalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel amountLabel = new JLabel("Rp. " + String.format("%,d", totalAmount));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        amountLabel.setForeground(new Color(40, 167, 69));
        amountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel("Silakan bayar di kasir saat pengambilan pesanan");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setForeground(new Color(100, 100, 100));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(cashIcon);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(totalLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(amountLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(instructionLabel);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton confirmButton = new JButton("Konfirmasi Pesanan");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setPreferredSize(new Dimension(160, 35));
        
        JButton cancelButton = new JButton("Batal");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(80, 35));
        
        confirmButton.addActionListener(e -> {
            paymentConfirmed = true;
            cashDialog.dispose();
            dispose();
        });
        
        cancelButton.addActionListener(e -> cashDialog.dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        cashDialog.add(headerPanel, BorderLayout.NORTH);
        cashDialog.add(contentPanel, BorderLayout.CENTER);
        cashDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        cashDialog.setVisible(true);
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(248, 249, 250));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        
        JButton cancelButton = new JButton("Batal");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> {
            selectedPaymentMethod = null;
            dispose();
        });
        
        bottomPanel.add(cancelButton);
        
        return bottomPanel;
    }
    
    private void setupEventHandlers() {
        // Prevent closing without selection
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                selectedPaymentMethod = null;
                dispose();
            }
        });
    }
    
    public String getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }
    
    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }
}