package view;

import controller.AuthController;
import controller.CustomerController;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.MenuItem;
import model.Order;
import model.OrderItem;

public class CustomerView extends JFrame {
    private AuthController authController;
    private CustomerController customerController;
    
    // Color Scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);
    private static final Color DARK_GRAY = new Color(52, 73, 94);
    private static final Color WHITE = Color.WHITE;
    
    // Components
    private JTabbedPane tabbedPane;
    
    // Menu Tab Components
    private JTable menuTable;
    private DefaultTableModel menuTableModel;
    private JComboBox<String> categoryComboBox;
    private JButton viewAllButton, viewByCategoryButton, addToCartButton;
    private JSpinner quantitySpinner;
    private JLabel cartStatusLabel;
    
    // Cart Tab Components
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalLabel;
    private JComboBox<Order.PaymentMethod> paymentComboBox;
    private JButton removeFromCartButton, clearCartButton, checkoutButton;
    
    // Cart data
    private List<OrderItem> cartItems;
    private BigDecimal cartTotal;
    
    public CustomerView(AuthController authController) {
        this.authController = authController;
        this.customerController = new CustomerController();
        this.cartItems = new ArrayList<>();
        this.cartTotal = BigDecimal.ZERO;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }
    
    private void initializeComponents() {
        setTitle("Dashboard Customer - " + authController.getCurrentUser().getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
        
        // Modern Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(WHITE);
        
        // Menu Tab Components
        String[] menuColumns = {"ID", "Nama Menu", "Kategori", "Harga", "Deskripsi"};
        menuTableModel = new DefaultTableModel(menuColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        menuTable = new JTable(menuTableModel);
        setupTableAppearance(menuTable);
        
        categoryComboBox = new JComboBox<>();
        styleComboBox(categoryComboBox);
        
        viewAllButton = createStyledButton("Lihat Semua Menu", SUCCESS_COLOR, "📋");
        viewByCategoryButton = createStyledButton("Filter Kategori", SECONDARY_COLOR, "🔍");
        addToCartButton = createStyledButton("Tambah ke Keranjang", WARNING_COLOR, "🛒");
        
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        styleSpinner(quantitySpinner);
        
        cartStatusLabel = new JLabel("Items di keranjang: 0");
        cartStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cartStatusLabel.setForeground(PRIMARY_COLOR);
        cartStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Cart Tab Components
        String[] cartColumns = {"Nama Menu", "Harga", "Qty", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cartTable = new JTable(cartTableModel);
        setupTableAppearance(cartTable);
        
        totalLabel = new JLabel("Total: Rp0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalLabel.setForeground(SUCCESS_COLOR);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        paymentComboBox = new JComboBox<>(Order.PaymentMethod.values());
        styleComboBox(paymentComboBox);
        
        removeFromCartButton = createStyledButton("Hapus dari Keranjang", DANGER_COLOR, "❌");
        clearCartButton = createStyledButton("Kosongkan Keranjang", DARK_GRAY, "🗑️");
        checkoutButton = createStyledButton("CHECKOUT", SUCCESS_COLOR, "💳");
        checkoutButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        checkoutButton.setPreferredSize(new Dimension(250, 60));
    }
    
    private JButton createStyledButton(String text, Color bgColor, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setBackground(bgColor);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBackground(WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        comboBox.setPreferredSize(new Dimension(150, 35));
    }
    
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        spinner.setPreferredSize(new Dimension(80, 35));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
    
    private void setupTableAppearance(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(LIGHT_GRAY);
        table.setSelectionBackground(new Color(52, 152, 219, 100));
        table.setSelectionForeground(DARK_GRAY);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Center alignment for numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (table == menuTable) {
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
            table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Kategori
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Harga
            
            // Column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(50);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.getColumnModel().getColumn(3).setPreferredWidth(100);
            table.getColumnModel().getColumn(4).setPreferredWidth(300);
        } else if (table == cartTable) {
            table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Harga
            table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Qty
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Subtotal
            
            // Column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(200);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(50);
            table.getColumnModel().getColumn(3).setPreferredWidth(100);
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Setup tabs
        setupMenuTab();
        setupCartTab();
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Title section
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("🍽️ DASHBOARD CUSTOMER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);
        
        JLabel userLabel = new JLabel("Welcome, " + authController.getCurrentUser().getFullName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(255, 255, 255, 200));
        
        titlePanel.add(titleLabel);
        
        // Right section with user info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        rightPanel.add(userLabel);
        rightPanel.add(Box.createHorizontalStrut(20));
        
        JButton logoutButton = createStyledButton("Logout", DANGER_COLOR, "🚪");
        logoutButton.addActionListener(e -> logout());
        rightPanel.add(logoutButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void setupMenuTab() {
        JPanel menuPanel = new JPanel(new BorderLayout(10, 10));
        menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        menuPanel.setBackground(WHITE);
        
        // Control Panel
        JPanel controlPanel = createControlPanel();
        
        // Table Panel
        JScrollPane menuScrollPane = new JScrollPane(menuTable);
        menuScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(LIGHT_GRAY, 2),
            "📋 Daftar Menu",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));
        menuScrollPane.setPreferredSize(new Dimension(0, 350));
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(WHITE);
        
        // Add to Cart Panel
        JPanel addToCartPanel = createAddToCartPanel();
        
        // Cart Status Panel
        JPanel cartStatusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cartStatusPanel.setBackground(new Color(39, 174, 96, 30));
        cartStatusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR, 2),
            new EmptyBorder(15, 20, 15, 20)
        ));
        cartStatusPanel.add(cartStatusLabel);
        
        bottomPanel.add(addToCartPanel, BorderLayout.NORTH);
        bottomPanel.add(cartStatusPanel, BorderLayout.SOUTH);
        
        menuPanel.add(controlPanel, BorderLayout.NORTH);
        menuPanel.add(menuScrollPane, BorderLayout.CENTER);
        menuPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("🍽️ Menu", menuPanel);
    }
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        controlPanel.setBackground(LIGHT_GRAY);
        controlPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "🔍 Filter Menu",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));
        
        JLabel categoryLabel = new JLabel("Kategori:");
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        categoryLabel.setForeground(DARK_GRAY);
        
        controlPanel.add(categoryLabel);
        controlPanel.add(categoryComboBox);
        controlPanel.add(viewByCategoryButton);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(viewAllButton);
        
        return controlPanel;
    }
    
    private JPanel createAddToCartPanel() {
        JPanel addToCartPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        addToCartPanel.setBackground(new Color(243, 156, 18, 30));
        addToCartPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(WARNING_COLOR, 2),
            "🛒 Tambah ke Keranjang",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            WARNING_COLOR
        ));
        
        JLabel quantityLabel = new JLabel("Jumlah:");
        quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        quantityLabel.setForeground(DARK_GRAY);
        
        addToCartPanel.add(quantityLabel);
        addToCartPanel.add(quantitySpinner);
        addToCartPanel.add(Box.createHorizontalStrut(20));
        addToCartPanel.add(addToCartButton);
        
        return addToCartPanel;
    }
    
    private void setupCartTab() {
        JPanel cartPanel = new JPanel(new BorderLayout(10, 10));
        cartPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        cartPanel.setBackground(WHITE);
        
        // Cart Table
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(LIGHT_GRAY, 2),
            "🛒 Keranjang Belanja",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));
        cartScrollPane.setPreferredSize(new Dimension(0, 300));
        
        // Cart Controls Panel
        JPanel cartControlsPanel = createCartControlsPanel();
        
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        cartPanel.add(cartControlsPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("🛒 Keranjang (0)", cartPanel);
    }
    
    private JPanel createCartControlsPanel() {
        JPanel cartControlsPanel = new JPanel(new BorderLayout(10, 10));
        cartControlsPanel.setBackground(WHITE);
        cartControlsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "💳 Kontrol Keranjang",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));
        
        // Top buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(removeFromCartButton);
        buttonPanel.add(clearCartButton);
        
        // Payment panel
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        paymentPanel.setBackground(WHITE);
        
        JLabel paymentLabel = new JLabel("Metode Pembayaran:");
        paymentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        paymentLabel.setForeground(DARK_GRAY);
        
        paymentPanel.add(paymentLabel);
        paymentPanel.add(paymentComboBox);
        
        // Checkout panel
        JPanel checkoutPanel = new JPanel(new BorderLayout(10, 10));
        checkoutPanel.setBackground(WHITE);
        checkoutPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.setBackground(new Color(39, 174, 96, 30));
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR, 2),
            new EmptyBorder(15, 30, 15, 30)
        ));
        totalPanel.add(totalLabel);
        
        JPanel checkoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        checkoutButtonPanel.setBackground(WHITE);
        checkoutButtonPanel.add(checkoutButton);
        
        checkoutPanel.add(totalPanel, BorderLayout.CENTER);
        checkoutPanel.add(checkoutButtonPanel, BorderLayout.SOUTH);
        
        cartControlsPanel.add(buttonPanel, BorderLayout.NORTH);
        cartControlsPanel.add(paymentPanel, BorderLayout.CENTER);
        cartControlsPanel.add(checkoutPanel, BorderLayout.SOUTH);
        
        return cartControlsPanel;
    }
    
    private void setupEventHandlers() {
        viewAllButton.addActionListener(e -> loadAllMenu());
        viewByCategoryButton.addActionListener(e -> loadMenuByCategory());
        addToCartButton.addActionListener(e -> addToCart());
        removeFromCartButton.addActionListener(e -> removeFromCart());
        clearCartButton.addActionListener(e -> clearCart());
        checkoutButton.addActionListener(e -> checkout());
        
        // Double click to show menu details
        menuTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showMenuDetails();
                }
            }
        });
        
        // Tab change listener to update cart count
        tabbedPane.addChangeListener(e -> updateCartTabTitle());
    }
    
    private void loadData() {
        loadAllMenu();
        loadCategories();
        updateCartDisplay();
    }
    
    private void loadAllMenu() {
        menuTableModel.setRowCount(0);
        List<MenuItem> menuItems = customerController.viewMenu();
        
        for (MenuItem item : menuItems) {
            Object[] row = {
                item.getId(),
                item.getName(),
                item.getCategory(),
                String.format("Rp%,d", item.getPrice().intValue()),
                item.getDescription()
            };
            menuTableModel.addRow(row);
        }
    }
    
    private void loadMenuByCategory() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        if (selectedCategory == null || selectedCategory.equals("Semua Kategori")) {
            loadAllMenu();
            return;
        }
        
        menuTableModel.setRowCount(0);
        List<MenuItem> menuItems = customerController.getMenuByCategory(selectedCategory);
        
        for (MenuItem item : menuItems) {
            Object[] row = {
                item.getId(),
                item.getName(),
                item.getCategory(),
                String.format("Rp%,d", item.getPrice().intValue()),
                item.getDescription()
            };
            menuTableModel.addRow(row);
        }
    }
    
    private void loadCategories() {
        categoryComboBox.removeAllItems();
        categoryComboBox.addItem("Semua Kategori");
        
        List<String> categories = customerController.getAvailableCategories();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
    }
    
    private void addToCart() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            showStyledMessage("Pilih menu yang akan ditambahkan ke keranjang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int menuId = (Integer) menuTableModel.getValueAt(selectedRow, 0);
        String menuName = (String) menuTableModel.getValueAt(selectedRow, 1);
        String priceText = (String) menuTableModel.getValueAt(selectedRow, 3);
        BigDecimal price = new BigDecimal(priceText.replace("Rp", "").replace(",", ""));
        
        int quantity = (Integer) quantitySpinner.getValue();
        
        // Check if item already exists in cart
        boolean found = false;
        for (OrderItem item : cartItems) {
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
            cartItems.add(orderItem);
        }
        
        updateCartDisplay();
        quantitySpinner.setValue(1);
        
        // Show confirmation
        showStyledMessage(
            String.format("✅ %s (x%d) berhasil ditambahkan ke keranjang!", menuName, quantity),
            "Berhasil Ditambahkan", 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Switch to cart tab if user wants
        int switchTab = JOptionPane.showConfirmDialog(this,
            "Ingin melihat keranjang sekarang?",
            "Lihat Keranjang",
            JOptionPane.YES_NO_OPTION);
        
        if (switchTab == JOptionPane.YES_OPTION) {
            tabbedPane.setSelectedIndex(1);
        }
    }
    
    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            showStyledMessage("Pilih item yang akan dihapus dari keranjang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String itemName = cartItems.get(selectedRow).getMenuItem().getName();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Hapus " + itemName + " dari keranjang?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            cartItems.remove(selectedRow);
            updateCartDisplay();
            showStyledMessage("✅ " + itemName + " dihapus dari keranjang.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void clearCart() {
        if (cartItems.isEmpty()) {
            showStyledMessage("Keranjang sudah kosong!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Kosongkan semua item dari keranjang?",
            "Konfirmasi Kosongkan Keranjang",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            cartItems.clear();
            updateCartDisplay();
            showStyledMessage("✅ Keranjang berhasil dikosongkan.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void checkout() {
        if (cartItems.isEmpty()) {
            showStyledMessage("Keranjang kosong! Tambahkan menu terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Order.PaymentMethod paymentMethod = (Order.PaymentMethod) paymentComboBox.getSelectedItem();
        
        // Show order summary
        StringBuilder orderSummary = new StringBuilder();
        orderSummary.append("═══════════════════════════════════════\n");
        orderSummary.append("           RINGKASAN PESANAN\n");
        orderSummary.append("═══════════════════════════════════════\n\n");
        
        for (OrderItem item : cartItems) {
            orderSummary.append(String.format("%-20s x%-3d = Rp%,d\n",
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getSubtotal().intValue()));
        }
        
        orderSummary.append("\n═══════════════════════════════════════\n");
        orderSummary.append(String.format("TOTAL: Rp%,d\n", cartTotal.intValue()));
        orderSummary.append("Metode Pembayaran: ").append(paymentMethod).append("\n");
        orderSummary.append("═══════════════════════════════════════\n\n");
        orderSummary.append("Lanjutkan dengan pemesanan?");
        
        int confirm = JOptionPane.showConfirmDialog(this,
            orderSummary.toString(),
            "Konfirmasi Pesanan",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            processCheckout(paymentMethod);
        }
    }
    
    private void processCheckout(Order.PaymentMethod paymentMethod) {
        // Handle different payment methods
        if (paymentMethod == Order.PaymentMethod.CASH) {
            showStyledMessage(
                "💰 Untuk pembayaran CASH, silakan bayar saat pengambilan pesanan.\n" +
                "Total yang harus dibayar: Rp" + String.format("%,d", cartTotal.intValue()),
                "Pembayaran CASH",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else if (paymentMethod == Order.PaymentMethod.QRIS) {
            // Simulate QRIS payment
            int qrisConfirm = JOptionPane.showConfirmDialog(this,
                "📱 Silakan scan QR Code untuk pembayaran QRIS.\n" +
                "Total: Rp" + String.format("%,d", cartTotal.intValue()) + "\n\n" +
                "Apakah pembayaran sudah berhasil?",
                "Pembayaran QRIS",
                JOptionPane.YES_NO_OPTION);
            
            if (qrisConfirm != JOptionPane.YES_OPTION) {
                showStyledMessage("❌ Pembayaran dibatalkan.", "Dibatalkan", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // Process the order
        boolean success = customerController.createOrder(
            authController.getCurrentUser().getId(),
            cartItems,
            paymentMethod
        );
        
        if (success) {
            // Show success message
            String successMessage = String.format(
                "🎉 PESANAN BERHASIL! 🎉\n\n" +
                "Terima kasih atas pesanan Anda!\n\n" +
                "═══════════════════════════════════════\n" +
                "Detail Pesanan:\n" +
                "Customer: %s\n" +
                "Total: Rp%,d\n" +
                "Pembayaran: %s\n" +
                "Status: COMPLETED\n" +
                "═══════════════════════════════════════\n\n" +
                "Pesanan Anda sedang diproses.\n" +
                "Silakan tunggu konfirmasi dari restoran.",
                authController.getCurrentUser().getFullName(),
                cartTotal.intValue(),
                paymentMethod
            );
            
            showStyledMessage(successMessage, "Pesanan Berhasil", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear cart after successful order
            cartItems.clear();
            updateCartDisplay();
            
            // Switch back to menu tab
            tabbedPane.setSelectedIndex(0);
            
        } else {
            showStyledMessage(
                "❌ Gagal memproses pesanan. Silakan coba lagi.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void updateCartDisplay() {
        // Update cart table
        cartTableModel.setRowCount(0);
        cartTotal = BigDecimal.ZERO;
        
        for (OrderItem item : cartItems) {
            Object[] row = {
                item.getMenuItem().getName(),
                String.format("Rp%,d", item.getUnitPrice().intValue()),
                item.getQuantity(),
                String.format("Rp%,d", item.getSubtotal().intValue())
            };
            cartTableModel.addRow(row);
            cartTotal = cartTotal.add(item.getSubtotal());
        }
        
        // Update total label
        totalLabel.setText("Total: Rp" + String.format("%,d", cartTotal.intValue()));
        
        // Update cart status label
        cartStatusLabel.setText("Items di keranjang: " + cartItems.size());
        
        // Update tab title
        updateCartTabTitle();
        
        // Enable/disable buttons
        checkoutButton.setEnabled(!cartItems.isEmpty());
        removeFromCartButton.setEnabled(!cartItems.isEmpty());
        clearCartButton.setEnabled(!cartItems.isEmpty());
    }
    
    private void updateCartTabTitle() {
        int itemCount = cartItems.size();
        tabbedPane.setTitleAt(1, "🛒 Keranjang (" + itemCount + ")");
        
        // Change tab color if there are items
        if (itemCount > 0) {
            tabbedPane.setBackgroundAt(1, new Color(255, 235, 59, 100));
        } else {
            tabbedPane.setBackgroundAt(1, null);
        }
    }
    
    private void showMenuDetails() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            String menuInfo = String.format(
                "═══════════════════════════════════════\n" +
                "              DETAIL MENU\n" +
                "═══════════════════════════════════════\n\n" +
                "🆔 ID: %s\n" +
                "🍽️ Nama: %s\n" +
                "📂 Kategori: %s\n" +
                "💰 Harga: %s\n" +
                "📝 Deskripsi: %s\n\n" +
                "═══════════════════════════════════════\n" +
                "Klik 'Tambah ke Keranjang' untuk memesan menu ini.",
                menuTableModel.getValueAt(selectedRow, 0),
                menuTableModel.getValueAt(selectedRow, 1),
                menuTableModel.getValueAt(selectedRow, 2),
                menuTableModel.getValueAt(selectedRow, 3),
                menuTableModel.getValueAt(selectedRow, 4)
            );
            
            showStyledMessage(menuInfo, "Detail Menu", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showStyledMessage(String message, String title, int messageType) {
        // Create custom dialog
        JDialog dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        
        // Message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        messagePanel.setBackground(WHITE);
        
        JTextArea textArea = new JTextArea(message);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        messagePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(WHITE);
        
        JButton okButton = createStyledButton("OK", PRIMARY_COLOR, "✅");
        okButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(okButton);
        
        dialog.add(messagePanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void logout() {
        // Check if there are items in cart
        if (!cartItems.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "⚠️ Anda memiliki " + cartItems.size() + " item di keranjang.\n" +
                "Item akan hilang jika logout. Yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "🚪 Yakin ingin logout?", 
            "Konfirmasi Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            authController.logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    public void showDashboard() {
        setVisible(true);
    }
    
}