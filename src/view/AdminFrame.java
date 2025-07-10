package view;

import controller.AdminController;
import controller.AuthController;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import model.MenuItem;
import model.User;

public class AdminFrame extends JFrame {
    private AuthController authController;
    private AdminController adminController;
    private JTabbedPane tabbedPane;
    
    // Employee Management Components
    private JTable employeeTable;
    private DefaultTableModel employeeTableModel;
    private JTextField empUsernameField, empPasswordField, empFullNameField, empEmailField, empPhoneField;
    private JComboBox<User.Role> empRoleComboBox;
    
    // Menu Management Components
    private JTable menuTable;
    private DefaultTableModel menuTableModel;
    private JTextField menuNameField, menuDescField, menuPriceField, menuCategoryField;
    private JCheckBox menuAvailableCheckBox;
    
    public AdminFrame(AuthController authController) {
        this.authController = authController;
        this.adminController = new AdminController();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }
    
    private void initializeComponents() {
        setTitle("Dashboard Admin - " + authController.getCurrentUser().getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Employee Management Tab
        setupEmployeeTab();
        
        // Menu Management Tab
        setupMenuTab();
    }
    
    private void setupEmployeeTab() {
        JPanel employeePanel = new JPanel(new BorderLayout(10, 10));
        employeePanel.setBackground(new Color(248, 249, 250));
        employeePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Modern table styling
        String[] empColumns = {"ID", "Username", "Role", "Nama Lengkap", "Email", "Phone"};
        employeeTableModel = new DefaultTableModel(empColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(employeeTableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeTable.setRowHeight(35);
        employeeTable.setGridColor(new Color(233, 236, 239));
        employeeTable.setSelectionBackground(new Color(52, 152, 219, 50));
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        employeeTable.getTableHeader().setBackground(new Color(52, 152, 219));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.getTableHeader().setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JScrollPane empScrollPane = new JScrollPane(employeeTable);
        empScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "👥 Daftar Karyawan",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(52, 152, 219)
        ));
        empScrollPane.setPreferredSize(new Dimension(0, 350));
        empScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Modern form panel
        JPanel empFormPanel = new JPanel(new GridBagLayout());
        empFormPanel.setBackground(Color.WHITE);
        empFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                "➕ Tambah Karyawan Baru",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(46, 204, 113)
            ),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Modern input fields
        empUsernameField = createModernTextField(15);
        empPasswordField = createModernTextField(15);
        empFullNameField = createModernTextField(15);
        empEmailField = createModernTextField(15);
        empPhoneField = createModernTextField(15);
        empRoleComboBox = new JComboBox<>(User.Role.values());
        empRoleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        empRoleComboBox.setPreferredSize(new Dimension(150, 35));
        
        // Layout form fields with modern labels
        addFormField(empFormPanel, gbc, "👤 Username:", empUsernameField, 0, 0);
        addFormField(empFormPanel, gbc, "🔒 Password:", empPasswordField, 2, 0);
        addFormField(empFormPanel, gbc, "📝 Nama Lengkap:", empFullNameField, 0, 1);
        addFormField(empFormPanel, gbc, "📧 Email:", empEmailField, 2, 1);
        addFormField(empFormPanel, gbc, "📱 Phone:", empPhoneField, 0, 2);
        addFormField(empFormPanel, gbc, "🏷️ Role:", empRoleComboBox, 2, 2);
        
        // Modern button
        JButton addEmpButton = new JButton("➕ Tambah Karyawan");
        addEmpButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addEmpButton.setBackground(new Color(46, 204, 113));
        addEmpButton.setForeground(Color.WHITE);
        addEmpButton.setBorderPainted(false);
        addEmpButton.setFocusPainted(false);
        addEmpButton.setPreferredSize(new Dimension(200, 40));
        addEmpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addEmpButton.addActionListener(e -> addEmployee());
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 8, 8, 8);
        empFormPanel.add(addEmpButton, gbc);
        
        employeePanel.add(empScrollPane, BorderLayout.CENTER);
        employeePanel.add(empFormPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("👥 Kelola Karyawan", employeePanel);
    }

    // Helper methods
    private JTextField createModernTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(150, 35));
        return field;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(52, 73, 94));
        
        gbc.gridx = x; gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(label, gbc);
        
        gbc.gridx = x + 1; gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }
    
    private void setupMenuTab() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        
        // Menu Table
        String[] menuColumns = {"ID", "Nama", "Kategori", "Harga", "Tersedia", "Deskripsi"};
        menuTableModel = new DefaultTableModel(menuColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(menuTableModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane menuScrollPane = new JScrollPane(menuTable);
        menuScrollPane.setPreferredSize(new Dimension(0, 300));
        
        // Menu Form
        JPanel menuFormPanel = new JPanel(new GridBagLayout());
        menuFormPanel.setBorder(BorderFactory.createTitledBorder("Kelola Menu"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        menuNameField = new JTextField(15);
        menuDescField = new JTextField(15);
        menuPriceField = new JTextField(15);
        menuCategoryField = new JTextField(15);
        menuAvailableCheckBox = new JCheckBox("Tersedia");
        menuAvailableCheckBox.setSelected(true);
        
        // Layout form fields
        gbc.gridx = 0; gbc.gridy = 0;
        menuFormPanel.add(new JLabel("Nama Menu:"), gbc);
        gbc.gridx = 1;
        menuFormPanel.add(menuNameField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        menuFormPanel.add(new JLabel("Kategori:"), gbc);
        gbc.gridx = 3;
        menuFormPanel.add(menuCategoryField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        menuFormPanel.add(new JLabel("Harga:"), gbc);
        gbc.gridx = 1;
        menuFormPanel.add(menuPriceField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        menuFormPanel.add(menuAvailableCheckBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        menuFormPanel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuFormPanel.add(menuDescField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton addMenuButton = new JButton("Tambah Menu");
        addMenuButton.setBackground(new Color(46, 204, 113));
        addMenuButton.setForeground(Color.WHITE);
        addMenuButton.addActionListener(e -> addMenuItem());
        
        JButton updateMenuButton = new JButton("Update Menu");
        updateMenuButton.setBackground(new Color(52, 152, 219));
        updateMenuButton.setForeground(Color.WHITE);
        updateMenuButton.addActionListener(e -> updateMenuItem());
        
        JButton deleteMenuButton = new JButton("Hapus Menu");
        deleteMenuButton.setBackground(new Color(231, 76, 60));
        deleteMenuButton.setForeground(Color.WHITE);
        deleteMenuButton.addActionListener(e -> deleteMenuItem());
        
        JButton clearMenuButton = new JButton("Clear Form");
        clearMenuButton.addActionListener(e -> clearMenuForm());
        
        buttonPanel.add(addMenuButton);
        buttonPanel.add(updateMenuButton);
        buttonPanel.add(deleteMenuButton);
        buttonPanel.add(clearMenuButton);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuFormPanel.add(buttonPanel, gbc);
        
        // Table selection listener
        menuTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedMenuItem();
            }
        });
        
        menuPanel.add(menuScrollPane, BorderLayout.CENTER);
        menuPanel.add(menuFormPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Kelola Menu", menuPanel);
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
            GradientPaint gp = new GradientPaint(0, 0, new Color(155, 89, 182), getWidth(), 0, new Color(142, 68, 173));
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
    
    JLabel iconLabel = new JLabel("👨‍💼");
    iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
    
    JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, -5));
    titlePanel.setOpaque(false);
    titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
    
    JLabel titleLabel = new JLabel("ADMIN DASHBOARD");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
    titleLabel.setForeground(Color.WHITE);
    
    JLabel userLabel = new JLabel("Welcome, " + authController.getCurrentUser().getFullName());
    userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    userLabel.setForeground(new Color(255, 255, 255, 200));
    
    titlePanel.add(titleLabel);
    titlePanel.add(userLabel);
    
    headerLeft.add(iconLabel);
    headerLeft.add(titlePanel);
    
    // Logout button with modern style
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
    
    // Modern tabbed pane
    tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
    tabbedPane.setBackground(Color.WHITE);
    tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    add(headerPanel, BorderLayout.NORTH);
    add(tabbedPane, BorderLayout.CENTER);
}
    
    private void setupEventHandlers() {
        // Already handled in individual methods
    }
    
    private void loadData() {
        loadEmployeeData();
        loadMenuData();
    }
    
    private void loadEmployeeData() {
        employeeTableModel.setRowCount(0);
        List<User> employees = adminController.getAllEmployees();
        
        for (User emp : employees) {
            Object[] row = {
                emp.getId(),
                emp.getUsername(),
                emp.getRole(),
                emp.getFullName(),
                emp.getEmail(),
                emp.getPhone()
            };
            employeeTableModel.addRow(row);
        }
    }
    
    private void loadMenuData() {
        menuTableModel.setRowCount(0);
        List<MenuItem> menuItems = adminController.getAllMenuItems();
        
        for (MenuItem item : menuItems) {
            Object[] row = {
                item.getId(),
                item.getName(),
                item.getCategory(),
                "Rp" + item.getPrice(),
                item.isAvailable() ? "Ya" : "Tidak",
                item.getDescription()
            };
            menuTableModel.addRow(row);
        }
    }
    
    private void addEmployee() {
        try {
            String username = empUsernameField.getText().trim();
            String password = empPasswordField.getText().trim();
            String fullName = empFullNameField.getText().trim();
            String email = empEmailField.getText().trim();
            String phone = empPhoneField.getText().trim();
            User.Role role = (User.Role) empRoleComboBox.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username, password, dan nama lengkap harus diisi!");
                return;
            }
            
            User user = new User(username, password, role, fullName, email);
            user.setPhone(phone);
            
            if (adminController.createEmployee(user)) {
                JOptionPane.showMessageDialog(this, "Karyawan berhasil ditambahkan!");
                clearEmployeeForm();
                loadEmployeeData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan karyawan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void addMenuItem() {
        try {
            String name = menuNameField.getText().trim();
            String description = menuDescField.getText().trim();
            String priceText = menuPriceField.getText().trim();
            String category = menuCategoryField.getText().trim();
            boolean available = menuAvailableCheckBox.isSelected();
            
            if (name.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama, harga, dan kategori harus diisi!");
                return;
            }
            
            BigDecimal price = new BigDecimal(priceText);
            MenuItem menuItem = new MenuItem(name, description, price, category);
            menuItem.setAvailable(available);
            
            if (adminController.addMenuItem(menuItem)) {
                JOptionPane.showMessageDialog(this, "Menu berhasil ditambahkan!");
                clearMenuForm();
                loadMenuData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan menu!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void updateMenuItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih menu yang akan diupdate!");
            return;
        }
        
        try {
            int id = (Integer) menuTableModel.getValueAt(selectedRow, 0);
            String name = menuNameField.getText().trim();
            String description = menuDescField.getText().trim();
            String priceText = menuPriceField.getText().trim();
            String category = menuCategoryField.getText().trim();
            boolean available = menuAvailableCheckBox.isSelected();
            
            if (name.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama, harga, dan kategori harus diisi!");
                return;
            }
            
            BigDecimal price = new BigDecimal(priceText);
            MenuItem menuItem = new MenuItem(name, description, price, category);
            menuItem.setId(id);
            menuItem.setAvailable(available);
            
            if (adminController.updateMenuItem(menuItem)) {
                JOptionPane.showMessageDialog(this, "Menu berhasil diupdate!");
                clearMenuForm();
                loadMenuData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate menu!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void deleteMenuItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih menu yang akan dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin menghapus menu ini?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (Integer) menuTableModel.getValueAt(selectedRow, 0);
            
            if (adminController.deleteMenuItem(id)) {
                JOptionPane.showMessageDialog(this, "Menu berhasil dihapus!");
                clearMenuForm();
                loadMenuData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus menu!");
            }
        }
    }
    
    private void loadSelectedMenuItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (Integer) menuTableModel.getValueAt(selectedRow, 0);
            MenuItem menuItem = adminController.getMenuItemById(id);
            
            if (menuItem != null) {
                menuNameField.setText(menuItem.getName());
                menuDescField.setText(menuItem.getDescription());
                menuPriceField.setText(menuItem.getPrice().toString());
                menuCategoryField.setText(menuItem.getCategory());
                menuAvailableCheckBox.setSelected(menuItem.isAvailable());
            }
        }
    }
    
    private void clearEmployeeForm() {
        empUsernameField.setText("");
        empPasswordField.setText("");
        empFullNameField.setText("");
        empEmailField.setText("");
        empPhoneField.setText("");
        empRoleComboBox.setSelectedIndex(0);
    }
    
    private void clearMenuForm() {
        menuNameField.setText("");
        menuDescField.setText("");
        menuPriceField.setText("");
        menuCategoryField.setText("");
        menuAvailableCheckBox.setSelected(true);
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
