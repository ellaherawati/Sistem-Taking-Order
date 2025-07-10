package view;

import controller.AuthController;
import controller.KasirController;
import model.User;
import model.MenuItem;
import model.OrderItem;
import model.Order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KasirView {
    private Scanner scanner;
    private AuthController authController;
    private KasirController kasirController;
    
    public KasirView(AuthController authController) {
        this.scanner = new Scanner(System.in);
        this.authController = authController;
        this.kasirController = new KasirController();
    }
    
    public void showDashboard() {
        while (authController.isLoggedIn()) {
            System.out.println("\n=================================");
            System.out.println("        DASHBOARD KASIR");
            System.out.println("=================================");
            System.out.println("1. Proses Pesanan Baru");
            System.out.println("2. Lihat Menu");
            System.out.println("3. Logout");
            System.out.print("Pilih menu: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    processNewOrder();
                    break;
                case 2:
                    viewMenu();
                    break;
                case 3:
                    authController.logout();
                    System.out.println("Logout berhasil!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    private void processNewOrder() {
        System.out.println("\n=== PROSES PESANAN BARU ===");
        
        // Select customer
        List<User> customers = kasirController.getCustomers();
        if (customers.isEmpty()) {
            System.out.println("Tidak ada customer terdaftar!");
            return;
        }
        
        System.out.println("Pilih Customer:");
        for (int i = 0; i < customers.size(); i++) {
            User customer = customers.get(i);
            System.out.printf("%d. %s (%s)%n", i + 1, customer.getFullName(), customer.getUsername());
        }
        System.out.print("Pilih customer: ");
        
        int customerChoice = getIntInput() - 1;
        if (customerChoice < 0 || customerChoice >= customers.size()) {
            System.out.println("Pilihan customer tidak valid!");
            return;
        }
        
        User selectedCustomer = customers.get(customerChoice);
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Add items to order
        while (true) {
            System.out.println("\n=== TAMBAH ITEM PESANAN ===");
            List<MenuItem> menuItems = kasirController.getAvailableMenuItems();
            
            if (menuItems.isEmpty()) {
                System.out.println("Tidak ada menu tersedia!");
                break;
            }
            
            // Display menu
            System.out.printf("%-5s %-25s %-15s %-12s%n", "No", "Nama", "Kategori", "Harga");
            System.out.println("=".repeat(60));
            
            for (int i = 0; i < menuItems.size(); i++) {
                MenuItem item = menuItems.get(i);
                System.out.printf("%-5d %-25s %-15s Rp%-10.0f%n",
                                 i + 1,
                                 item.getName(),
                                 item.getCategory(),
                                 item.getPrice());
            }
            
            System.out.print("\nPilih menu (0 untuk selesai): ");
            int menuChoice = getIntInput() - 1;
            
            if (menuChoice == -1) {
                break;
            }
            
            if (menuChoice < 0 || menuChoice >= menuItems.size()) {
                System.out.println("Pilihan menu tidak valid!");
                continue;
            }
            
            MenuItem selectedItem = menuItems.get(menuChoice);
            
            System.out.print("Jumlah: ");
            int quantity = getIntInput();
            
            if (quantity <= 0) {
                System.out.println("Jumlah harus lebih dari 0!");
                continue;
            }
            
            OrderItem orderItem = new OrderItem(0, selectedItem.getId(), quantity, selectedItem.getPrice());
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
            
            System.out.printf("Ditambahkan: %s x%d = Rp%.0f%n", 
                             selectedItem.getName(), quantity, orderItem.getSubtotal());
            System.out.printf("Total sementara: Rp%.0f%n", totalAmount);
        }
        
        if (orderItems.isEmpty()) {
            System.out.println("Tidak ada item dalam pesanan!");
            return;
        }
        
        // Display order summary
        System.out.println("\n=== RINGKASAN PESANAN ===");
        System.out.println("Customer: " + selectedCustomer.getFullName());
        System.out.printf("%-25s %-8s %-12s %-12s%n", "Item", "Qty", "Harga", "Subtotal");
        System.out.println("=".repeat(60));
        
        for (OrderItem item : orderItems) {
            MenuItem menuItem = kasirController.getMenuItemById(item.getMenuItemId());
            System.out.printf("%-25s %-8d Rp%-10.0f Rp%-10.0f%n",
                             menuItem.getName(),
                             item.getQuantity(),
                             item.getUnitPrice(),
                             item.getSubtotal());
        }
        System.out.println("=".repeat(60));
        System.out.printf("TOTAL: Rp%.0f%n", totalAmount);
        
        // Select payment method
        System.out.println("\nMetode Pembayaran:");
        System.out.println("1. CASH");
        System.out.println("2. QRIS");
        System.out.print("Pilih metode pembayaran: ");
        
        int paymentChoice = getIntInput();
        Order.PaymentMethod paymentMethod;
        
        switch (paymentChoice) {
            case 1:
                paymentMethod = Order.PaymentMethod.CASH;
                break;
            case 2:
                paymentMethod = Order.PaymentMethod.QRIS;
                break;
            default:
                System.out.println("Metode pembayaran tidak valid!");
                return;
        }
        
        // Process payment
        System.out.print("Konfirmasi pesanan? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.toLowerCase().startsWith("y")) {
            int orderId = kasirController.processOrder(
                selectedCustomer.getId(),
                authController.getCurrentUser().getId(),
                orderItems,
                paymentMethod
            );
            
            if (orderId > 0) {
                System.out.println("\n=== PESANAN BERHASIL ===");
                System.out.println("Order ID: " + orderId);
                System.out.println("Total: Rp" + totalAmount);
                System.out.println("Metode Pembayaran: " + paymentMethod);
                System.out.println("Status: COMPLETED");
                
                if (paymentMethod == Order.PaymentMethod.CASH) {
                    System.out.print("Jumlah uang diterima: Rp");
                    BigDecimal cashReceived = getBigDecimalInput();
                    BigDecimal change = cashReceived.subtract(totalAmount);
                    
                    if (change.compareTo(BigDecimal.ZERO) >= 0) {
                        System.out.printf("Kembalian: Rp%.0f%n", change);
                    } else {
                        System.out.println("Uang tidak cukup!");
                    }
                }
            } else {
                System.out.println("Gagal memproses pesanan!");
            }
        } else {
            System.out.println("Pesanan dibatalkan.");
        }
    }
    
    private void viewMenu() {
        System.out.println("\n=== DAFTAR MENU ===");
        List<MenuItem> menuItems = kasirController.getAvailableMenuItems();
        
        if (menuItems.isEmpty()) {
            System.out.println("Tidak ada menu tersedia.");
            return;
        }
        
        String currentCategory = "";
        for (MenuItem item : menuItems) {
            if (!item.getCategory().equals(currentCategory)) {
                currentCategory = item.getCategory();
                System.out.println("\n=== " + currentCategory.toUpperCase() + " ===");
                System.out.printf("%-25s %-12s %-30s%n", "Nama", "Harga", "Deskripsi");
                System.out.println("-".repeat(70));
            }
            
            System.out.printf("%-25s Rp%-10.0f %-30s%n",
                             item.getName(),
                             item.getPrice(),
                             item.getDescription() != null ? item.getDescription() : "");
        }
    }
    
    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
            return -1;
        }
    }
    
    private BigDecimal getBigDecimalInput() {
        try {
            return new BigDecimal(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
            return BigDecimal.ZERO;
        }
    }
}
