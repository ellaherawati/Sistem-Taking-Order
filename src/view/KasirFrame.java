package view;

import controller.AuthController;
import controller.KasirController;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.User;

public class KasirFrame extends JFrame {
    private AuthController authController;
    private KasirController kasirController;
    
    // Components
    private JTable menuTable;
    private DefaultTableModel menuTableModel;
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JComboBox<User> customerComboBox;
    private JComboBox<Order.PaymentMethod> paymentComboBox;
    private JLabel totalLabel;
    private JSpinner quantitySpinner;
    private JButton addToOrderButton, removeFromOrderButton, processOrderButton;
    
    private List<OrderItem> currentOrderItems;
    private BigDecimal currentTotal;
    
    public KasirFrame(AuthController authController) {
        this.authController = authController;
        this.kasirController = new KasirController();
        this.currentOrderItems = new ArrayList<>();
        this.currentTotal = BigDecimal.ZERO;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }
    
    private void initializeComponents() {
        setTitle("Dashboard Kasir - " + authController.getCurrentUser().getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Menu Table
        String[] menuColumns = {"ID", "Nama", "Kategori", "Harga", "Deskripsi"};
        menuTableModel = new DefaultTableModel(menuColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(menuTableModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Order Table
        String[] orderColumns = {"Nama Menu", "Harga", "Qty", "Subtotal"};
        orderTableModel = new DefaultTableModel(orderColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Other components
        customerComboBox = new JComboBox<>();
        paymentComboBox = new JComboBox<>(Order.PaymentMethod.values());
        totalLabel = new JLabel("Total: Rp0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        
        addToOrderButton = new JButton("Tambah ke Pesanan");
        addToOrderButton.setBackground(new Color(46, 204, 113));
        addToOrderButton.setForeground(Color.WHITE);
        
        removeFromOrderButton = new JButton("Hapus dari Pesanan");
        removeFromOrderButton.setBackground(new Color(231, 76, 60));
        removeFromOrderButton.setForeground(Color.WHITE);
        
        processOrderButton = new JButton("Proses Pesanan");
        processOrderButton.setBackground(new Color(52, 152, 219));
        processOrderButton.setForeground(Color.WHITE);
        processOrderButton.setFont(new Font("Arial", Font.BOLD, 14));
    }
    
    private void setupLayout() {
    setLayout(new BorderLayout());
    getContentPane().setBackground(new Color(248, 249, 250));
    
    // Modern Header with gradient
    JPanel headerPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gp = new GradientPaint(0, 0, new Color(46, 204, 113), getWidth(), 0, new Color(39, 174, 96));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    headerPanel.setPreferredSize(new Dimension(0, 80));
    headerPanel.setLayout(new BorderLayout());
    headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
    
    // Header content
    JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    headerLeft.setOpaque(false);
    
    JLabel iconLabel = new JLabel("💰");
    iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
    
    JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, -5));
    titlePanel.setOpaque(false);
    titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
    
    JLabel titleLabel = new JLabel("KASIR DASHBOARD");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
    titleLabel.setForeground(Color.WHITE);
    
    JLabel userLabel = new JLabel("Operator: " + authController.getCurrentUser().getFullName());
    userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    userLabel.setForeground(new Color(255, 255, 255, 200));
    
    titlePanel.add(titleLabel);
    titlePanel.add(userLabel);
    
    headerLeft.add(iconLabel);
    headerLeft.add(titlePanel);
    
    // Logout button
    JButton logoutButton = new JButton("🚪 Logout");
    logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
    logoutButton.setBackground(new Color(231, 76, 60));
    logoutButton.setForeground(Color.WHITE);
    logoutButton.setBorderPainted(false);
    logoutButton.setFocusPainted(false);
    logoutButton.setPreferredSize(new Dimension(100, 35));
    logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    logoutButton.addActionListener(e -> logout());
    
    headerPanel.add(headerLeft, BorderLayout.WEST);
    headerPanel.add(logoutButton, BorderLayout.EAST);
    
    // Main Panel with modern cards
    JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
    mainPanel.setBackground(new Color(248, 249, 250));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // Left Panel - Menu (Modern Card)
    JPanel leftPanel = createModernCard("🍽️ Daftar Menu", new Color(52, 152, 219));
    leftPanel.setPreferredSize(new Dimension(550, 0));
    leftPanel.setLayout(new BorderLayout(10, 10));
    
    // Modern table styling
    menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    menuTable.setRowHeight(40);
    menuTable.setGridColor(new Color(233, 236, 239));
    menuTable.setSelectionBackground(new Color(52, 152, 219, 50));
    menuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    menuTable.getTableHeader().setBackground(new Color(52, 152, 219));
    menuTable.getTableHeader().setForeground(Color.WHITE);
    
    JScrollPane menuScrollPane = new JScrollPane(menuTable);
    menuScrollPane.setBorder(BorderFactory.createEmptyBorder());
    menuScrollPane.getViewport().setBackground(Color.WHITE);
    leftPanel.add(menuScrollPane, BorderLayout.CENTER);
    
    // Add to order panel
    JPanel addPanel = new JPanel(new FlowLayout());
    addPanel.setBackground(Color.WHITE);
    addPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    
    JLabel qtyLabel = new JLabel("📦 Qty:");
    qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
    qtyLabel.setForeground(new Color(52, 73, 94));
    
    quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    quantitySpinner.setPreferredSize(new Dimension(60, 30));
    
    addToOrderButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
    addToOrderButton.setPreferredSize(new Dimension(180, 35));
    addToOrderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    addToOrderButton.setBorderPainted(false);
    addToOrderButton.setFocusPainted(false);
    
    addPanel.add(qtyLabel);
    addPanel.add(quantitySpinner);
    addPanel.add(addToOrderButton);
    leftPanel.add(addPanel, BorderLayout.SOUTH);
    
    // Right Panel - Order (Modern Card)
    JPanel rightPanel = createModernCard("🛒 Pesanan Saat Ini", new Color(230, 126, 34));
    rightPanel.setLayout(new BorderLayout(10, 10));
    
    // Order table styling
    orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    orderTable.setRowHeight(35);
    orderTable.setGridColor(new Color(233, 236, 239));
    orderTable.setSelectionBackground(new Color(230, 126, 34, 50));
    orderTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    orderTable.getTableHeader().setBackground(new Color(230, 126, 34));
    orderTable.getTableHeader().setForeground(Color.WHITE);
    
    JScrollPane orderScrollPane = new JScrollPane(orderTable);
    orderScrollPane.setBorder(BorderFactory.createEmptyBorder());
    orderScrollPane.setPreferredSize(new Dimension(0, 300));
    orderScrollPane.getViewport().setBackground(Color.WHITE);
    rightPanel.add(orderScrollPane, BorderLayout.CENTER);
    
    // Order controls with modern styling
    JPanel orderControlPanel = new JPanel(new GridBagLayout());
    orderControlPanel.setBackground(Color.WHITE);
    orderControlPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 5, 8, 5);
    gbc.anchor = GridBagConstraints.WEST;
    
    // Modern combo boxes and labels
    JLabel customerLabel = new JLabel("👤 Customer:");
    customerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
    customerLabel.setForeground(new Color(52, 73, 94));
    
    customerComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    customerComboBox.setPreferredSize(new Dimension(200, 30));
    
    JLabel paymentLabel = new JLabel("💳 Pembayaran:");
    paymentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
    paymentLabel.setForeground(new Color(52, 73, 94));
    
    paymentComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    paymentComboBox.setPreferredSize(new Dimension(200, 30));
    
    // Layout controls
    gbc.gridx = 0; gbc.gridy = 0;
    orderControlPanel.add(customerLabel, gbc);
    gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
    orderControlPanel.add(customerComboBox, gbc);
    
    gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
    orderControlPanel.add(paymentLabel, gbc);
    gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
    orderControlPanel.add(paymentComboBox, gbc);
    
    // Modern buttons
    removeFromOrderButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
    removeFromOrderButton.setBorderPainted(false);
    removeFromOrderButton.setFocusPainted(false);
    removeFromOrderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    removeFromOrderButton.setPreferredSize(new Dimension(180, 30));
    
    gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    orderControlPanel.add(removeFromOrderButton, gbc);
    
    // Total label with modern styling
    totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    totalLabel.setForeground(new Color(46, 204, 113));
    totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
    totalLabel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
        BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    
    gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
    gbc.insets = new Insets(15, 5, 15, 5);
    orderControlPanel.add(totalLabel, gbc);
    
    // Process button
    processOrderButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    processOrderButton.setBorderPainted(false);
    processOrderButton.setFocusPainted(false);
    processOrderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    processOrderButton.setPreferredSize(new Dimension(200, 45));
    
    gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
    orderControlPanel.add(processOrderButton, gbc);
    
    rightPanel.add(orderControlPanel, BorderLayout.SOUTH);
    
    mainPanel.add(leftPanel, BorderLayout.WEST);
    mainPanel.add(rightPanel, BorderLayout.CENTER);
    
    add(headerPanel, BorderLayout.NORTH);
    add(mainPanel, BorderLayout.CENTER);
}

// Helper method to create modern cards
private JPanel createModernCard(String title, Color accentColor) {
    JPanel card = new JPanel();
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
        BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(3, 0, 0, 0, accentColor),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        )
    ));
    
    // Add title
    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    titleLabel.setForeground(accentColor);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    
    card.setLayout(new BorderLayout());
    card.add(titleLabel, BorderLayout.NORTH);
    
    return card;
}
    
    private void setupEventHandlers() {
        addToOrderButton.addActionListener(e -> addToOrder());
        removeFromOrderButton.addActionListener(e -> removeFromOrder());
        processOrderButton.addActionListener(e -> processOrder());
    }
    
    private void loadData() {
        loadMenuData();
        loadCustomerData();
    }
    
    private void loadMenuData() {
        menuTableModel.setRowCount(0);
        List<MenuItem> menuItems = kasirController.getAvailableMenuItems();
        
        for (MenuItem item : menuItems) {
            Object[] row = {
                item.getId(),
                item.getName(),
                item.getCategory(),
                "Rp" + item.getPrice(),
                item.getDescription()
            };
            menuTableModel.addRow(row);
        }
    }
    
    private void loadCustomerData() {
        customerComboBox.removeAllItems();
        List<User> customers = kasirController.getCustomers();
        
        for (User customer : customers) {
            customerComboBox.addItem(customer);
        }
    }
    
    private void addToOrder() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih menu yang akan ditambahkan!");
            return;
        }
        
        int menuId = (Integer) menuTableModel.getValueAt(selectedRow, 0);
        String menuName = (String) menuTableModel.getValueAt(selectedRow, 1);
        String priceText = (String) menuTableModel.getValueAt(selectedRow, 3);
        BigDecimal price = new BigDecimal(priceText.replace("Rp", ""));
        
        int quantity = (Integer) quantitySpinner.getValue();
        
        // Check if item already exists in order
        boolean found = false;
        for (OrderItem item : currentOrderItems) {
            if (item.getMenuItemId() == menuId) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        
        if (!found) {
            OrderItem orderItem = new OrderItem(0, menuId, quantity, price);
            MenuItem menuItem = new MenuItem();
            menuItem.setId(menuId);
            menuItem.setName(menuName);
            menuItem.setPrice(price);
            orderItem.setMenuItem(menuItem);
            currentOrderItems.add(orderItem);
        }
        
        updateOrderTable();
        quantitySpinner.setValue(1);
    }
    
    private void removeFromOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item yang akan dihapus dari pesanan!");
            return;
        }
        
        currentOrderItems.remove(selectedRow);
        updateOrderTable();
    }
    
    private void updateOrderTable() {
        orderTableModel.setRowCount(0);
        currentTotal = BigDecimal.ZERO;
        
        for (OrderItem item : currentOrderItems) {
            Object[] row = {
                item.getMenuItem().getName(),
                "Rp" + item.getUnitPrice(),
                item.getQuantity(),
                "Rp" + item.getSubtotal()
            };
            orderTableModel.addRow(row);
            currentTotal = currentTotal.add(item.getSubtotal());
        }
        
        totalLabel.setText("Total: Rp" + currentTotal);
    }
    
    private void processOrder() {
        if (currentOrderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pesanan kosong!");
            return;
        }
        
        User selectedCustomer = (User) customerComboBox.getSelectedItem();
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Pilih customer!");
            return;
        }
        
        Order.PaymentMethod paymentMethod = (Order.PaymentMethod) paymentComboBox.getSelectedItem();
        
        // Show confirmation dialog
        String message = String.format(
            "Konfirmasi Pesanan:\n\n" +
            "Customer: %s\n" +
            "Total: Rp%s\n" +
            "Pembayaran: %s\n\n" +
            "Proses pesanan ini?",
            selectedCustomer.getFullName(),
            currentTotal,
            paymentMethod
        );
        
        int confirm = JOptionPane.showConfirmDialog(this, message, "Konfirmasi Pesanan", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Handle cash payment
            if (paymentMethod == Order.PaymentMethod.CASH) {
                String cashInput = JOptionPane.showInputDialog(this, "Masukkan jumlah uang yang diterima:");
                if (cashInput != null && !cashInput.trim().isEmpty()) {
                    try {
                        BigDecimal cashReceived = new BigDecimal(cashInput);
                        BigDecimal change = cashReceived.subtract(currentTotal);
                        
                        if (change.compareTo(BigDecimal.ZERO) < 0) {
                            JOptionPane.showMessageDialog(this, "Uang tidak cukup!");
                            return;
                        }
                        
                        // Process the order
                        int orderId = kasirController.processOrder(
                            selectedCustomer.getId(),
                            authController.getCurrentUser().getId(),
                            currentOrderItems,
                            paymentMethod
                        );
                        
                        if (orderId > 0) {
                            String successMessage = String.format(
                                "Pesanan berhasil diproses!\n\n" +
                                "Order ID: %d\n" +
                                "Total: Rp%s\n" +
                                "Uang Diterima: Rp%s\n" +
                                "Kembalian: Rp%s",
                                orderId, currentTotal, cashReceived, change
                            );
                            JOptionPane.showMessageDialog(this, successMessage, "Pesanan Berhasil", JOptionPane.INFORMATION_MESSAGE);
                            clearOrder();
                        } else {
                            JOptionPane.showMessageDialog(this, "Gagal memproses pesanan!");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Jumlah uang harus berupa angka!");
                    }
                }
            } else {
                // QRIS payment
                JOptionPane.showMessageDialog(this, "Silakan scan QR Code untuk pembayaran QRIS");
                
                int orderId = kasirController.processOrder(
                    selectedCustomer.getId(),
                    authController.getCurrentUser().getId(),
                    currentOrderItems,
                    paymentMethod
                );
                
                if (orderId > 0) {
                    String successMessage = String.format(
                        "Pesanan berhasil diproses!\n\n" +
                        "Order ID: %d\n" +
                        "Total: Rp%s\n" +
                        "Pembayaran: QRIS",
                        orderId, currentTotal
                    );
                    JOptionPane.showMessageDialog(this, successMessage, "Pesanan Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    clearOrder();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal memproses pesanan!");
                }
            }
        }
    }
    
    private void clearOrder() {
        currentOrderItems.clear();
        updateOrderTable();
        menuTable.clearSelection();
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin logout?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            authController.logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
