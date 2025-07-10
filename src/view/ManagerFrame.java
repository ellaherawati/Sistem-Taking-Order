package view;

import controller.AuthController;
import controller.ManagerController;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Order;

public class ManagerFrame extends JFrame {
    private AuthController authController;
    private ManagerController managerController;
    
    // Components
    private JTable reportTable;
    private DefaultTableModel reportTableModel;
    private JLabel totalSalesLabel, totalOrdersLabel, avgOrderLabel;
    private JTextField startDateField, endDateField;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ManagerFrame(AuthController authController) {
        this.authController = authController;
        this.managerController = new ManagerController();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("Dashboard Manager - " + authController.getCurrentUser().getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Report Table
        String[] columns = {"Order ID", "Tanggal", "Customer ID", "Kasir ID", "Total", "Pembayaran", "Status"};
        reportTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(reportTableModel);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Summary Labels - hapus prefix text
        totalSalesLabel = new JLabel("Rp0");
        totalSalesLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalSalesLabel.setForeground(new Color(46, 204, 113));

        totalOrdersLabel = new JLabel("0");
        totalOrdersLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalOrdersLabel.setForeground(new Color(52, 152, 219));

        avgOrderLabel = new JLabel("Rp0");
        avgOrderLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        avgOrderLabel.setForeground(new Color(155, 89, 182));
        
        // Date fields
        startDateField = new JTextField(10);
        endDateField = new JTextField(10);
        startDateField.setText(LocalDate.now().format(dateFormatter));
        endDateField.setText(LocalDate.now().format(dateFormatter));
        
        // Load today's data by default
        loadCustomReport(LocalDate.now(), LocalDate.now());
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
                GradientPaint gp = new GradientPaint(0, 0, new Color(230, 126, 34), getWidth(), 0, new Color(211, 84, 0));
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
        
        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, -5));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        JLabel titleLabel = new JLabel("MANAGER DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Sales Analytics - " + authController.getCurrentUser().getFullName());
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
        
        // Create main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(248, 249, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create custom report content directly
        createCustomReportContent(mainPanel);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void createCustomReportContent(JPanel parentPanel) {
        // Modern Controls Panel dengan 2 baris untuk menghindari tertutup
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, new Color(230, 126, 34)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            )
        ));
        
        // Baris pertama - Input tanggal dan generate button
        JPanel firstRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        firstRowPanel.setOpaque(false);
        
        JLabel filterIcon = new JLabel("📊");
        filterIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        JLabel titleLabel = new JLabel("Laporan Penjualan (Range Tanggal):");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(52, 73, 94));
        
        // Date fields with modern styling
        startDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        startDateField.setPreferredSize(new Dimension(120, 30));
        startDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        endDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        endDateField.setPreferredSize(new Dimension(120, 30));
        endDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        // Generate button
        JButton loadButton = new JButton("📊 Generate Laporan");
        loadButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loadButton.setBackground(new Color(230, 126, 34));
        loadButton.setForeground(Color.WHITE);
        loadButton.setBorderPainted(false);
        loadButton.setFocusPainted(false);
        loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadButton.setPreferredSize(new Dimension(150, 32));
        
        firstRowPanel.add(filterIcon);
        firstRowPanel.add(titleLabel);
        firstRowPanel.add(new JLabel("Dari:"));
        firstRowPanel.add(startDateField);
        firstRowPanel.add(new JLabel("Sampai:"));
        firstRowPanel.add(endDateField);
        firstRowPanel.add(loadButton);
        
        // Baris kedua - Quick access buttons
        JPanel secondRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        secondRowPanel.setOpaque(false);
        
        JLabel quickLabel = new JLabel("Quick Access:");
        quickLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        quickLabel.setForeground(new Color(52, 73, 94));
        
        JButton todayButton = new JButton("Hari Ini");
        todayButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        todayButton.setBackground(new Color(155, 89, 182));
        todayButton.setForeground(Color.WHITE);
        todayButton.setBorderPainted(false);
        todayButton.setFocusPainted(false);
        todayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        todayButton.setPreferredSize(new Dimension(80, 30));
        
        JButton last7DaysButton = new JButton("7 Hari Terakhir");
        last7DaysButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        last7DaysButton.setBackground(new Color(46, 204, 113));
        last7DaysButton.setForeground(Color.WHITE);
        last7DaysButton.setBorderPainted(false);
        last7DaysButton.setFocusPainted(false);
        last7DaysButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        last7DaysButton.setPreferredSize(new Dimension(120, 30));
        
        JButton last30DaysButton = new JButton("30 Hari Terakhir");
        last30DaysButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        last30DaysButton.setBackground(new Color(52, 152, 219));
        last30DaysButton.setForeground(Color.WHITE);
        last30DaysButton.setBorderPainted(false);
        last30DaysButton.setFocusPainted(false);
        last30DaysButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        last30DaysButton.setPreferredSize(new Dimension(120, 30));
        
        secondRowPanel.add(quickLabel);
        secondRowPanel.add(todayButton);
        secondRowPanel.add(last7DaysButton);
        secondRowPanel.add(last30DaysButton);
        
        // Event handlers
        last7DaysButton.addActionListener(e -> {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(6);
            startDateField.setText(startDate.format(dateFormatter));
            endDateField.setText(endDate.format(dateFormatter));
            loadCustomReport(startDate, endDate);
        });
        
        last30DaysButton.addActionListener(e -> {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(29);
            startDateField.setText(startDate.format(dateFormatter));
            endDateField.setText(endDate.format(dateFormatter));
            loadCustomReport(startDate, endDate);
        });
        
        todayButton.addActionListener(e -> {
            LocalDate today = LocalDate.now();
            startDateField.setText(today.format(dateFormatter));
            endDateField.setText(today.format(dateFormatter));
            loadCustomReport(today, today);
        });
        
        loadButton.addActionListener(e -> {
            try {
                LocalDate startDate = LocalDate.parse(startDateField.getText(), dateFormatter);
                LocalDate endDate = LocalDate.parse(endDateField.getText(), dateFormatter);
                
                if (startDate.isAfter(endDate)) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Tanggal mulai tidak boleh setelah tanggal selesai!",
                        "Error Range Tanggal", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                loadCustomReport(startDate, endDate);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Format tanggal tidak valid!\n\nGunakan format: yyyy-MM-dd\nContoh: " + LocalDate.now().format(dateFormatter),
                    "Error Format Tanggal", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Gabungkan kedua baris dalam control panel
        controlPanel.add(firstRowPanel, BorderLayout.NORTH);
        controlPanel.add(secondRowPanel, BorderLayout.SOUTH);
        
        // Summary Panel
        JPanel summaryPanel = createSummaryPanel();
        
        // Modern Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, new Color(52, 152, 219)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        ));
        
        JLabel tableTitle = new JLabel("📋 Detail Transaksi");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(new Color(52, 152, 219));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Modern table styling
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportTable.setRowHeight(35);
        reportTable.setGridColor(new Color(233, 236, 239));
        reportTable.setSelectionBackground(new Color(230, 126, 34, 50));
        reportTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        reportTable.getTableHeader().setBackground(new Color(230, 126, 34));
        reportTable.getTableHeader().setForeground(Color.WHITE);
        reportTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        parentPanel.add(controlPanel, BorderLayout.NORTH);
        parentPanel.add(summaryPanel, BorderLayout.CENTER);
        parentPanel.add(tablePanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        summaryPanel.setOpaque(false);
        
        // Sales Card
        JPanel salesCard = createStatsCard("💰", "Total Penjualan", totalSalesLabel, new Color(46, 204, 113));
        
        // Orders Card  
        JPanel ordersCard = createStatsCard("📋", "Total Pesanan", totalOrdersLabel, new Color(52, 152, 219));
        
        // Average Card
        JPanel avgCard = createStatsCard("📊", "Rata-rata", avgOrderLabel, new Color(155, 89, 182));
        
        summaryPanel.add(salesCard);
        summaryPanel.add(ordersCard);
        summaryPanel.add(avgCard);
        
        return summaryPanel;
    }

    private JPanel createStatsCard(String icon, String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Icon and title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(108, 117, 125));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        
        // Value
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Ubah dari 16 ke 20
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0)); // Tambah padding
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(248, 249, 250));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
    
    private void setupEventHandlers() {
        // Table selection listener for order details
        reportTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showOrderDetails();
            }
        });
    }
    
    private void loadCustomReport(LocalDate startDate, LocalDate endDate) {
        try {
            System.out.println("Loading custom report from: " + startDate + " to: " + endDate);
            List<Order> orders = managerController.getCustomSalesReport(startDate, endDate);
            BigDecimal totalSales = managerController.getCustomSalesTotal(startDate, endDate);
            int totalOrders = managerController.getCustomOrderCount(startDate, endDate);
            
            System.out.println("Found " + orders.size() + " orders, total sales: " + totalSales);
            
            updateReportTable(orders);
            updateSummary(totalSales, totalOrders);
            
            if (orders.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "📊 Tidak ada transaksi dari " + startDate.format(dateFormatter) + 
                    " sampai " + endDate.format(dateFormatter) + "\n\n" +
                    "💡 Tips:\n" +
                    "• Pastikan sudah ada transaksi di periode tersebut\n" +
                    "• Coba buat transaksi melalui Kasir terlebih dahulu\n" +
                    "• Atau jalankan script sample data",
                    "Tidak Ada Data", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "❌ Error saat memuat laporan custom:\n" + e.getMessage(),
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateReportTable(List<Order> orders) {
        reportTableModel.setRowCount(0);
        
        for (Order order : orders) {
            Object[] row = {
                order.getId(),
                order.getOrderDate().toLocalDateTime().toLocalDate().format(dateFormatter),
                order.getCustomerId(),
                order.getKasirId(),
                String.format("Rp%,.0f", order.getTotalAmount()),
                order.getPaymentMethod(),
                order.getStatus()
            };
            reportTableModel.addRow(row);
        }
    }
    
    private void updateSummary(BigDecimal totalSales, int totalOrders) {
        totalSalesLabel.setText(String.format("Rp%,.0f", totalSales));
        totalOrdersLabel.setText(String.valueOf(totalOrders));
        
        if (totalOrders > 0) {
            BigDecimal avgOrder = totalSales.divide(BigDecimal.valueOf(totalOrders), BigDecimal.ROUND_HALF_UP);
            avgOrderLabel.setText(String.format("Rp%,.0f", avgOrder));
        } else {
            avgOrderLabel.setText("Rp0");
        }
    }
    
    private void showOrderDetails() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderInfo = String.format(
                "🧾 DETAIL PESANAN\n\n" +
                "🆔 Order ID: %s\n" +
                "📅 Tanggal: %s\n" +
                "👤 Customer ID: %s\n" +
                "🏪 Kasir ID: %s\n" +
                "💰 Total: %s\n" +
                "💳 Pembayaran: %s\n" +
                "✅ Status: %s\n\n" +
                "💡 Klik baris lain untuk melihat detail pesanan lainnya.",
                reportTableModel.getValueAt(selectedRow, 0),
                reportTableModel.getValueAt(selectedRow, 1),
                reportTableModel.getValueAt(selectedRow, 2),
                reportTableModel.getValueAt(selectedRow, 3),
                reportTableModel.getValueAt(selectedRow, 4),
                reportTableModel.getValueAt(selectedRow, 5),
                reportTableModel.getValueAt(selectedRow, 6)
            );
            
            JOptionPane.showMessageDialog(this, orderInfo, "📋 Detail Pesanan", JOptionPane.INFORMATION_MESSAGE);
        }
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
