package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CustomerFrame extends JFrame {
    // Data untuk menu items
    private List<MenuItem> menuItems;
    private List<OrderItem> orderItems;
    private JLabel totalLabel;
    private JPanel cartPanel;
    private JScrollPane cartScrollPane;
    private NumberFormat currencyFormat;
    
    // Menu item class
    static class MenuItem {
        String name;
        int price;
        String description;
        String category;
        String imagePath;
        ImageIcon imageIcon;
        
        MenuItem(String name, int price, String description, String category, String imagePath) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.category = category;
            this.imagePath = imagePath;
            this.imageIcon = loadImage(imagePath);
        }
        
        private ImageIcon loadImage(String path) {
            try {
                if (path != null && !path.isEmpty()) {
                    // Coba load dari file
                    File imageFile = new File(path);
                    if (imageFile.exists()) {
                        BufferedImage img = ImageIO.read(imageFile);
                        // Resize gambar ke ukuran yang diinginkan
                        Image scaledImg = img.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImg);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading image: " + path + " - " + e.getMessage());
            }
            
            // Jika gagal load gambar, return null (akan menggunakan placeholder)
            return null;
        }
    }
    
    // Order item class
    static class OrderItem {
        MenuItem menuItem;
        int quantity;
        
        OrderItem(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }
    }
    
    // Method untuk logout
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin keluar?", 
            "Konfirmasi Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            dispose(); // Menutup frame
        }
    }

    public CustomerFrame() {
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        initializeMenuData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        // Window closing handler
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logout();
            }
        });
    }
    
    private void initializeMenuData() {
        menuItems = new ArrayList<>();
        orderItems = new ArrayList<>();
        
        // MAKANAN - 20 items
        menuItems.add(new MenuItem("Nasi Goreng", 50000, 
            "Nasi yang digoreng dengan bumbu khas, ditambahkan telur mata sapi, irisan mentimun, dan kerupuk merah.", 
            "MAKANAN", "images/nasi_goreng.jpg"));
        
        menuItems.add(new MenuItem("Ramen", 70000, 
            "Mie kuah khas Jepang dengan kuah gurih, topping telur rebus, irisan daging, dan daun bawang.", 
            "MAKANAN", "images/ramen.jpg"));
        
        menuItems.add(new MenuItem("Katsu", 80000, 
            "Daging ayam yang dibalut tepung renyah, digoreng garing dan disajikan dengan saus katsu manis gurih.", 
            "MAKANAN", "images/katsu.jpg"));
        
        menuItems.add(new MenuItem("Rendang", 75000, 
            "Masakan daging sapi dengan santan dan rempah-rempah khas Minangkabau yang dimasak dalam waktu lama.", 
            "MAKANAN", "images/rendang.jpg"));
        
        menuItems.add(new MenuItem("Gado-gado", 45000, 
            "Salad sayuran Indonesia dengan saus kacang yang gurih, dilengkapi dengan kerupuk dan telur rebus.", 
            "MAKANAN", "images/gado_gado.jpg"));
        
        menuItems.add(new MenuItem("Soto Ayam", 40000, 
            "Sup ayam tradisional dengan kuah bening, dilengkapi dengan nasi, telur, dan perasan jeruk nipis.", 
            "MAKANAN", "images/soto_ayam.jpg"));
        
        menuItems.add(new MenuItem("Mie Ayam", 35000, 
            "Mie kuah dengan potongan ayam, pangsit, dan sayuran segar yang disajikan dengan kuah kaldu gurih.", 
            "MAKANAN", "images/mie_ayam.jpg"));
        
        menuItems.add(new MenuItem("Nasi Padang", 55000, 
            "Nasi putih dengan berbagai lauk khas Padang seperti rendang, gulai, dan sambal yang pedas.", 
            "MAKANAN", "images/nasi_padang.jpg"));
        
        menuItems.add(new MenuItem("Ayam Penyet", 60000, 
            "Ayam goreng yang dipenyet dengan sambal terasi pedas, disajikan dengan lalapan dan nasi hangat.", 
            "MAKANAN", "images/ayam_penyet.jpg"));
        
        menuItems.add(new MenuItem("Bakso", 30000, 
            "Bola daging sapi dalam kuah kaldu segar, disajikan dengan mie, tahu, dan sayuran hijau.", 
            "MAKANAN", "images/bakso.jpg"));
        
        menuItems.add(new MenuItem("Satay", 65000, 
            "Tusuk sate daging sapi atau ayam yang dibakar dengan bumbu kacang dan kecap manis.", 
            "MAKANAN", "images/satay.jpg"));
        
        menuItems.add(new MenuItem("Gudeg", 50000, 
            "Makanan khas Yogyakarta dari nangka muda yang dimasak dengan santan dan gula merah.", 
            "MAKANAN", "images/gudeg.jpg"));
        
        menuItems.add(new MenuItem("Pecel Lele", 35000, 
            "Ikan lele goreng yang disajikan dengan sambal pedas dan lalapan segar.", 
            "MAKANAN", "images/pecel_lele.jpg"));
        
        menuItems.add(new MenuItem("Nasi Bakar", 45000, 
            "Nasi yang dibungkus daun pisang dan dibakar, disajikan dengan ayam dan sambal.", 
            "MAKANAN", "images/nasi_bakar.jpg"));
        
        menuItems.add(new MenuItem("Pempek", 40000, 
            "Makanan khas Palembang dari ikan dan tepung sagu, disajikan dengan kuah cuka pedas.", 
            "MAKANAN", "images/pempek.jpg"));
        
        menuItems.add(new MenuItem("Rawon", 55000, 
            "Sup daging sapi dengan kuah hitam khas Jawa Timur, disajikan dengan nasi dan kerupuk.", 
            "MAKANAN", "images/rawon.jpg"));
        
        menuItems.add(new MenuItem("Beef Steak", 85000, 
            "Daging sapi panggang dengan saus lada hitam, disajikan dengan kentang goreng dan salad.", 
            "MAKANAN", "images/beef_steak.jpg"));
        
        menuItems.add(new MenuItem("Ikan Bakar", 70000, 
            "Ikan segar yang dibakar dengan bumbu rempah, disajikan dengan nasi dan sambal.", 
            "MAKANAN", "images/ikan_bakar.jpg"));
        
        menuItems.add(new MenuItem("Capcay", 35000, 
            "Tumis sayuran campur dengan saus tiram yang gurih, cocok untuk vegetarian.", 
            "MAKANAN", "images/capcay.jpg"));
        
        menuItems.add(new MenuItem("Ayam Teriyaki", 65000, 
            "Ayam panggang dengan saus teriyaki manis khas Jepang, disajikan dengan nasi putih.", 
            "MAKANAN", "images/ayam_teriyaki.jpg"));
        
        // MINUMAN - 15 items
        menuItems.add(new MenuItem("Ice Caramel", 30000, 
            "Minuman dingin berbasis kopi dengan karamel, cocok untuk pencinta rasa creamy dan menyegarkan.", 
            "MINUMAN", "images/ice_caramel.jpg"));
        
        menuItems.add(new MenuItem("Ice Tea", 25000, 
            "Teh dingin yang segar, manis, pas, dan cocok jadi pendamping segala menu makanan.", 
            "MINUMAN", "images/ice_tea.jpg"));
        
        menuItems.add(new MenuItem("Cappuccino", 35000, 
            "Kopi espresso dengan susu steamed yang creamy, ditaburi bubuk coklat di atasnya.", 
            "MINUMAN", "images/cappuccino.jpg"));
        
        menuItems.add(new MenuItem("Latte", 40000, 
            "Kopi espresso dengan susu steamed yang lebih banyak, rasa yang mild dan creamy.", 
            "MINUMAN", "images/latte.jpg"));
        
        menuItems.add(new MenuItem("Americano", 28000, 
            "Kopi hitam klasik dengan rasa yang kuat, perfect untuk pencinta kopi murni.", 
            "MINUMAN", "images/americano.jpg"));
        
        menuItems.add(new MenuItem("Matcha Latte", 42000, 
            "Teh hijau matcha premium dengan susu steamed, rasa yang unik dan menyegarkan.", 
            "MINUMAN", "images/matcha_latte.jpg"));
        
        menuItems.add(new MenuItem("Chocolate", 35000, 
            "Minuman coklat hangat yang creamy, perfect untuk cuaca dingin dan mood booster.", 
            "MINUMAN", "images/chocolate.jpg"));
        
        menuItems.add(new MenuItem("Jus Jeruk", 25000, 
            "Jus jeruk segar yang kaya vitamin C, menyegarkan dan menyehatkan.", 
            "MINUMAN", "images/jus_jeruk.jpg"));
        
        menuItems.add(new MenuItem("Jus Alpukat", 30000, 
            "Jus alpukat creamy dengan susu, kaya nutrisi dan sangat mengenyangkan.", 
            "MINUMAN", "images/jus_alpukat.jpg"));
        
        menuItems.add(new MenuItem("Smoothie Mangga", 32000, 
            "Smoothie mangga segar dengan yogurt, manis dan menyegarkan di cuaca panas.", 
            "MINUMAN", "images/smoothie_mangga.jpg"));
        
        menuItems.add(new MenuItem("Es Kelapa Muda", 20000, 
            "Air kelapa muda segar yang alami, sangat menyegarkan dan kaya elektrolit.", 
            "MINUMAN", "images/es_kelapa_muda.jpg"));
        
        menuItems.add(new MenuItem("Lemon Tea", 22000, 
            "Teh dengan perasan lemon segar, rasa yang menyegarkan dan sedikit asam.", 
            "MINUMAN", "images/lemon_tea.jpg"));
        
        menuItems.add(new MenuItem("Milkshake Vanilla", 38000, 
            "Milkshake vanilla creamy dengan topping whipped cream dan cherry.", 
            "MINUMAN", "images/milkshake_vanilla.jpg"));
        
        menuItems.add(new MenuItem("Iced Mocha", 45000, 
            "Kombinasi kopi dan coklat dengan es, topped dengan whipped cream yang lezat.", 
            "MINUMAN", "images/iced_mocha.jpg"));
        
        menuItems.add(new MenuItem("Teh Tarik", 28000, 
            "Teh susu khas Malaysia yang ditarik untuk menghasilkan busa yang creamy.", 
            "MINUMAN", "images/teh_tarik.jpg"));
    }
    
    private void initializeComponents() {
        setTitle("Dapur Arunika - Taking Order System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set background
        getContentPane().setBackground(new Color(240, 240, 240));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // Left Panel - Menu
        JPanel menuPanel = createMenuPanel();
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setPreferredSize(new Dimension(700, 600));
        menuScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        menuScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        menuScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Right Panel - Order Summary
        JPanel orderPanel = createOrderPanel();
        
        mainPanel.add(menuScrollPane, BorderLayout.CENTER);
        mainPanel.add(orderPanel, BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome to");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Restaurant name
        JLabel restaurantLabel = new JLabel("Dapur Arunika");
        restaurantLabel.setForeground(Color.WHITE);
        restaurantLabel.setFont(new Font("Serif", Font.BOLD, 36));
        restaurantLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Header content
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(new Color(70, 130, 180));
        headerContent.add(welcomeLabel, BorderLayout.NORTH);
        headerContent.add(restaurantLabel, BorderLayout.CENTER);
        
        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.addActionListener(e -> logout());
        
        headerPanel.add(headerContent, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Makanan section
        JLabel makananLabel = new JLabel("MAKANAN");
        makananLabel.setFont(new Font("Arial", Font.BOLD, 20));
        makananLabel.setForeground(new Color(60, 60, 60));
        makananLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuPanel.add(makananLabel);
        menuPanel.add(Box.createVerticalStrut(15));
        
        // Makanan items - menggunakan grid yang dapat menyesuaikan dengan jumlah item
        int makananCount = 0;
        for (MenuItem item : menuItems) {
            if (item.category.equals("MAKANAN")) {
                makananCount++;
            }
        }
        
        // Buat grid untuk makanan (4 kolom)
        int makananRows = (int) Math.ceil(makananCount / 4.0);
        JPanel makananGrid = new JPanel(new GridLayout(makananRows, 4, 15, 15));
        makananGrid.setBackground(Color.WHITE);
        
        for (MenuItem item : menuItems) {
            if (item.category.equals("MAKANAN")) {
                makananGrid.add(createMenuItemPanel(item));
            }
        }
        
        makananGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuPanel.add(makananGrid);
        menuPanel.add(Box.createVerticalStrut(30));
        
        // Minuman section
        JLabel minumanLabel = new JLabel("MINUMAN");
        minumanLabel.setFont(new Font("Arial", Font.BOLD, 20));
        minumanLabel.setForeground(new Color(60, 60, 60));
        minumanLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuPanel.add(minumanLabel);
        menuPanel.add(Box.createVerticalStrut(15));
        
        // Minuman items - menggunakan grid yang dapat menyesuaikan dengan jumlah item
        int minumanCount = 0;
        for (MenuItem item : menuItems) {
            if (item.category.equals("MINUMAN")) {
                minumanCount++;
            }
        }
        
        // Buat grid untuk minuman (3 kolom)
        int minumanRows = (int) Math.ceil(minumanCount / 3.0);
        JPanel minumanGrid = new JPanel(new GridLayout(minumanRows, 3, 15, 15));
        minumanGrid.setBackground(Color.WHITE);
        
        for (MenuItem item : menuItems) {
            if (item.category.equals("MINUMAN")) {
                minumanGrid.add(createMenuItemPanel(item));
            }
        }
        
        minumanGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuPanel.add(minumanGrid);
        
        return menuPanel;
    }
    
    private JPanel createMenuItemPanel(MenuItem item) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        itemPanel.setPreferredSize(new Dimension(200, 280));
        
        // Image label - menggunakan gambar asli atau placeholder
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 100));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (item.imageIcon != null) {
            // Jika ada gambar, gunakan gambar tersebut
            imageLabel.setIcon(item.imageIcon);
        } else {
            // Jika tidak ada gambar, gunakan placeholder
            imageLabel.setBackground(new Color(240, 240, 240));
            imageLabel.setOpaque(true);
            imageLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            imageLabel.setText("📸");
            imageLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        }
        
        // Item info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(60, 60, 60));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel priceLabel = new JLabel("Rp. " + String.format("%,d", item.price));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(40, 167, 69));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea descArea = new JTextArea(item.description);
        descArea.setFont(new Font("Arial", Font.PLAIN, 11));
        descArea.setForeground(new Color(100, 100, 100));
        descArea.setBackground(Color.WHITE);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add button
        JButton addButton = new JButton("➕");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setPreferredSize(new Dimension(40, 40));
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addToOrder(item));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addButton);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(descArea);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(buttonPanel);
        
        itemPanel.add(imageLabel, BorderLayout.NORTH);
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        
        return itemPanel;
    }
    
    private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBackground(new Color(248, 249, 250));
        orderPanel.setPreferredSize(new Dimension(350, 600));
        orderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Order title
        JLabel orderTitle = new JLabel("Pesanan");
        orderTitle.setFont(new Font("Arial", Font.BOLD, 24));
        orderTitle.setForeground(new Color(60, 60, 60));
        orderTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Cart panel
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(new Color(248, 249, 250));
        
        cartScrollPane = new JScrollPane(cartPanel);
        cartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        cartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cartScrollPane.setBorder(BorderFactory.createEmptyBorder());
        cartScrollPane.setBackground(new Color(248, 249, 250));
        
        // Total panel
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(248, 249, 250));
        totalPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel totalTextLabel = new JLabel("Total pesanan");
        totalTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalTextLabel.setForeground(new Color(60, 60, 60));
        
        totalLabel = new JLabel("Rp. 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(40, 167, 69));
        
        totalPanel.add(totalTextLabel, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);
        
        // Checkout button
        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBackground(new Color(248, 249, 250));
        
        JButton checkoutButton = new JButton("KERANJANG");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setBackground(new Color(40, 167, 69));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.setBorderPainted(false);
        checkoutButton.setPreferredSize(new Dimension(300, 50));
        checkoutButton.addActionListener(e -> checkout());
        
        checkoutPanel.add(checkoutButton, BorderLayout.CENTER);
        
        orderPanel.add(orderTitle, BorderLayout.NORTH);
        orderPanel.add(cartScrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(248, 249, 250));
        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        bottomPanel.add(checkoutPanel, BorderLayout.CENTER);
        
        orderPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return orderPanel;
    }
    
    private void addToOrder(MenuItem item) {
        // Check if item already exists in order
        for (OrderItem orderItem : orderItems) {
            if (orderItem.menuItem.name.equals(item.name)) {
                orderItem.quantity++;
                updateOrderDisplay();
                return;
            }
        }
        
        // Add new item
        orderItems.add(new OrderItem(item, 1));
        updateOrderDisplay();
    }
    
    private void updateOrderDisplay() {
        cartPanel.removeAll();
        
        if (orderItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("Belum ada pesanan");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(new Color(150, 150, 150));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cartPanel.add(emptyLabel);
        } else {
            for (OrderItem orderItem : orderItems) {
                cartPanel.add(createOrderItemPanel(orderItem));
                cartPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        updateTotal();
        cartPanel.revalidate();
        cartPanel.repaint();
    }
    
    private JPanel createOrderItemPanel(OrderItem orderItem) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        itemPanel.setMaximumSize(new Dimension(300, 80));
        
        // Item info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(orderItem.menuItem.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel priceLabel = new JLabel("Rp. " + String.format("%,d", orderItem.menuItem.price));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(100, 100, 100));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        
        // Quantity controls
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        quantityPanel.setBackground(Color.WHITE);
        
        JButton minusButton = new JButton("kurang");
        minusButton.setFont(new Font("Arial", Font.PLAIN, 12));
        minusButton.setPreferredSize(new Dimension(30, 30));
        minusButton.setBackground(new Color(220, 53, 69));
        minusButton.setForeground(Color.WHITE);
        minusButton.setFocusPainted(false);
        minusButton.setBorderPainted(false);
        minusButton.addActionListener(e -> decreaseQuantity(orderItem));
        
        JLabel quantityLabel = new JLabel(String.valueOf(orderItem.quantity));
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quantityLabel.setPreferredSize(new Dimension(30, 30));
        
        JButton plusButton = new JButton("tambah");
        plusButton.setFont(new Font("Arial", Font.PLAIN, 12));
        plusButton.setPreferredSize(new Dimension(30, 30));
        plusButton.setBackground(new Color(40, 167, 69));
        plusButton.setForeground(Color.WHITE);
        plusButton.setFocusPainted(false);
        plusButton.setBorderPainted(false);
        plusButton.addActionListener(e -> increaseQuantity(orderItem));
        
        quantityPanel.add(minusButton);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(plusButton);
        
        itemPanel.add(infoPanel, BorderLayout.WEST);
        itemPanel.add(quantityPanel, BorderLayout.EAST);
        
        return itemPanel;
    }
    
    private void increaseQuantity(OrderItem orderItem) {
        orderItem.quantity++;
        updateOrderDisplay();
    }
    
    private void decreaseQuantity(OrderItem orderItem) {
        if (orderItem.quantity > 1) {
            orderItem.quantity--;
        } else {
            orderItems.remove(orderItem);
        }
        updateOrderDisplay();
    }
    
    private void updateTotal() {
        int total = 0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.menuItem.price * orderItem.quantity;
        }
        totalLabel.setText("Rp. " + String.format("%,d", total));
    }
    
    // Method checkout() yang diperbarui untuk CustomerFrame.java
    private void checkout() {
        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Hitung total
        int total = 0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.menuItem.price * orderItem.quantity;
        }
        
        // Buat ringkasan pesanan
        StringBuilder orderSummary = new StringBuilder("Ringkasan Pesanan:\n\n");
        for (OrderItem orderItem : orderItems) {
            int itemTotal = orderItem.menuItem.price * orderItem.quantity;
            orderSummary.append(String.format("%s x%d - Rp. %,d\n", 
                orderItem.menuItem.name, orderItem.quantity, itemTotal));
        }
        orderSummary.append("\nTotal: Rp. ").append(String.format("%,d", total));
        orderSummary.append("\n\nLanjutkan ke pembayaran?");
        
        // Konfirmasi pesanan
        int choice = JOptionPane.showConfirmDialog(this, orderSummary.toString(), 
            "Konfirmasi Pesanan", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Pesanan berhasil dibuat
            JOptionPane.showMessageDialog(this, "Pesanan berhasil dibuat!\nSilakan memilih metode pembayaran.", 
                "Pesanan Berhasil", JOptionPane.INFORMATION_MESSAGE);
    
            // Tampilkan dialog metode pembayaran
            PaymentMethodDialog paymentDialog = new PaymentMethodDialog(this, total);
            paymentDialog.setVisible(true);
    
            // Ambil metode pembayaran yang dipilih
            String metodeBayar = paymentDialog.getSelectedPaymentMethod();
    
            if (metodeBayar != null) {
                JOptionPane.showMessageDialog(this, "Anda memilih metode pembayaran: " + metodeBayar);
                // Lanjutkan proses bayar sesuai metodeBayar, misalnya simpan data pembayaran dll
            } else {
                JOptionPane.showMessageDialog(this, "Pembayaran dibatalkan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
    
            // Bersihkan keranjang dan update tampilan
            orderItems.clear();
            updateOrderDisplay();
        }
    }
    private class PaymentMethodDialog extends JDialog {
        private int totalAmount;
        private String selectedPaymentMethod;

        public PaymentMethodDialog(Frame parent, int totalAmount) {
            super(parent, "Metode Pembayaran", true);
            this.totalAmount = totalAmount;

        setSize(400, 250);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(
            "<html><center><h2>Metode Pembayaran</h2>Total: Rp " + String.format("%,d", totalAmount) + "</center></html>",
            SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Panel tombol metode pembayaran
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));

        // Tombol Cash
        JButton cashButton = new JButton("Cash");
        cashButton.setPreferredSize(new Dimension(120, 80));
        cashButton.addActionListener(e -> {
            selectedPaymentMethod = "Cash";
            dispose();
        });

        // Tombol QRIS
        JButton qrisButton = new JButton("QRIS");
        qrisButton.setPreferredSize(new Dimension(120, 80));
        qrisButton.addActionListener(e -> {
            selectedPaymentMethod = "QRIS";
            showQRISPopup(totalAmount);
            dispose();
        });

        btnPanel.add(cashButton);
        btnPanel.add(qrisButton);

        panel.add(btnPanel, BorderLayout.CENTER);

        // Tombol batal
        JButton cancelButton = new JButton("Batal");
        cancelButton.addActionListener(e -> {
            selectedPaymentMethod = null;
            dispose();
        });
        JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancelPanel.add(cancelButton);
        panel.add(cancelPanel, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    public String getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    private void showQRISPopup(int total) {
        // Generate kode barcode random sebagai string
        String randomBarcode = "QRIS-" + (int)(Math.random() * 1000000);

        JDialog qrisDialog = new JDialog(this, "Pembayaran QRIS", true);
        qrisDialog.setSize(350, 300);
        qrisDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Label barcode (sebagai teks)
        JLabel barcodeLabel = new JLabel(randomBarcode, SwingConstants.CENTER);
        barcodeLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        barcodeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        barcodeLabel.setPreferredSize(new Dimension(300, 100));

        // Label rincian pembayaran
        JLabel detailLabel = new JLabel(
            "<html><center>Total pembayaran:<br>Rp " + String.format("%,d", total) + "<br><br>" +
            "Silakan scan barcode di atas menggunakan aplikasi QRIS Anda.</center></html>",
            SwingConstants.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> qrisDialog.dispose());

        contentPanel.add(barcodeLabel, BorderLayout.NORTH);
        contentPanel.add(detailLabel, BorderLayout.CENTER);
        contentPanel.add(okButton, BorderLayout.SOUTH);

        qrisDialog.setContentPane(contentPanel);
        qrisDialog.setVisible(true);
    }

    
    
    private void setupEventHandlers() {
        // Window closing handler
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logout();
            }
        });
    }
    
    // Method untuk logout
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin keluar?", 
            "Konfirmasi Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            dispose(); // Menutup frame
        }
    }

    // Main method untuk testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
                                        new CustomerFrame().setVisible(true);
                        });
                    }
                }
            }
        
    