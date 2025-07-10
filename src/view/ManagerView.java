package view;

import controller.AuthController;
import controller.ManagerController;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import model.Order;
import model.OrderItem;

public class ManagerView {
    private Scanner scanner;
    private AuthController authController;
    private ManagerController managerController;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ManagerView(AuthController authController) {
        this.scanner = new Scanner(System.in);
        this.authController = authController;
        this.managerController = new ManagerController();
    }
    
    public void showDashboard() {
        while (authController.isLoggedIn()) {
            System.out.println("\n=================================");
            System.out.println("       DASHBOARD MANAGER");
            System.out.println("=================================");
            System.out.println("1. Laporan Penjualan Harian");
            System.out.println("2. Laporan Penjualan Mingguan");
            System.out.println("3. Laporan Penjualan Bulanan");
            System.out.println("4. Laporan Custom (Range Tanggal)");
            System.out.println("5. Logout");
            System.out.print("Pilih menu: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    showDailySalesReport();
                    break;
                case 2:
                    showWeeklySalesReport();
                    break;
                case 3:
                    showMonthlySalesReport();
                    break;
                case 4:
                    showCustomSalesReport();
                    break;
                case 5:
                    authController.logout();
                    System.out.println("Logout berhasil!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    private void showDailySalesReport() {
        System.out.println("\n=== LAPORAN PENJUALAN HARIAN ===");
        System.out.print("Masukkan tanggal (yyyy-MM-dd) atau tekan Enter untuk hari ini: ");
        String dateInput = scanner.nextLine().trim();
        
        LocalDate date;
        if (dateInput.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateInput, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Format tanggal tidak valid!");
                return;
            }
        }
        
        List<Order> orders = managerController.getDailySalesReport(date);
        BigDecimal totalSales = managerController.getDailySalesTotal(date);
        int orderCount = managerController.getDailyOrderCount(date);
        
        System.out.println("\nLAPORAN HARIAN - " + date.format(dateFormatter));
        System.out.println("=".repeat(80));
        System.out.println("Total Pesanan: " + orderCount);
        System.out.printf("Total Penjualan: Rp%.0f%n", totalSales);
        
        if (!orders.isEmpty()) {
            System.out.printf("Rata-rata per Pesanan: Rp%.0f%n", 
                             totalSales.divide(BigDecimal.valueOf(orderCount), BigDecimal.ROUND_HALF_UP));
        }
        
        displayOrderDetails(orders);
    }
    
    private void showWeeklySalesReport() {
        System.out.println("\n=== LAPORAN PENJUALAN MINGGUAN ===");
        System.out.print("Masukkan tanggal dalam minggu (yyyy-MM-dd) atau tekan Enter untuk minggu ini: ");
        String dateInput = scanner.nextLine().trim();
        
        LocalDate date;
        if (dateInput.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateInput, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Format tanggal tidak valid!");
                return;
            }
        }
        
        List<Order> orders = managerController.getWeeklySalesReport(date);
        BigDecimal totalSales = managerController.getWeeklySalesTotal(date);
        int orderCount = managerController.getDailyOrderCount(date);
        
        System.out.println("\nLAPORAN MINGGUAN - Minggu yang mengandung " + date.format(dateFormatter));
        System.out.println("=".repeat(80));
        System.out.println("Total Pesanan: " + orderCount);
        System.out.printf("Total Penjualan: Rp%.0f%n", totalSales);
        
        if (!orders.isEmpty()) {
            System.out.printf("Rata-rata per Pesanan: Rp%.0f%n", 
                             totalSales.divide(BigDecimal.valueOf(orderCount), BigDecimal.ROUND_HALF_UP));
        }
        
        displayOrderDetails(orders);
    }
    
    private void showMonthlySalesReport() {
        System.out.println("\n=== LAPORAN PENJUALAN BULANAN ===");
        System.out.print("Masukkan tanggal dalam bulan (yyyy-MM-dd) atau tekan Enter untuk bulan ini: ");
        String dateInput = scanner.nextLine().trim();
        
        LocalDate date;
        if (dateInput.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateInput, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Format tanggal tidak valid!");
                return;
            }
        }
        
        List<Order> orders = managerController.getMonthlySalesReport(date);
        BigDecimal totalSales = managerController.getMonthlySalesTotal(date);
        int orderCount = managerController.getDailyOrderCount(date);
        
        System.out.println("\nLAPORAN BULANAN - " + date.getMonth() + " " + date.getYear());
        System.out.println("=".repeat(80));
        System.out.println("Total Pesanan: " + orderCount);
        System.out.printf("Total Penjualan: Rp%.0f%n", totalSales);
        
        if (!orders.isEmpty()) {
            System.out.printf("Rata-rata per Pesanan: Rp%.0f%n", 
                             totalSales.divide(BigDecimal.valueOf(orderCount), BigDecimal.ROUND_HALF_UP));
        }
        
        displayOrderDetails(orders);
    }
    
    private void showCustomSalesReport() {
        System.out.println("\n=== LAPORAN CUSTOM (RANGE TANGGAL) ===");
        
        System.out.print("Tanggal mulai (yyyy-MM-dd): ");
        String startDateInput = scanner.nextLine().trim();
        
        System.out.print("Tanggal selesai (yyyy-MM-dd): ");
        String endDateInput = scanner.nextLine().trim();
        
        LocalDate startDate, endDate;
        try {
            startDate = LocalDate.parse(startDateInput, dateFormatter);
            endDate = LocalDate.parse(endDateInput, dateFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Format tanggal tidak valid!");
            return;
        }
        
        if (startDate.isAfter(endDate)) {
            System.out.println("Tanggal mulai tidak boleh setelah tanggal selesai!");
            return;
        }
        
        List<Order> orders = managerController.getCustomSalesReport(startDate, endDate);
        BigDecimal totalSales = managerController.getCustomSalesTotal(startDate, endDate);
        int orderCount = managerController.getCustomOrderCount(startDate, endDate);
        
        System.out.println("\nLAPORAN CUSTOM - " + startDate.format(dateFormatter) + 
                          " s/d " + endDate.format(dateFormatter));
        System.out.println("=".repeat(80));
        System.out.println("Total Pesanan: " + orderCount);
        System.out.printf("Total Penjualan: Rp%.0f%n", totalSales);
        
        if (!orders.isEmpty()) {
            System.out.printf("Rata-rata per Pesanan: Rp%.0f%n", 
                             totalSales.divide(BigDecimal.valueOf(orderCount), BigDecimal.ROUND_HALF_UP));
        }
        
        displayOrderDetails(orders);
    }
    
    private void displayOrderDetails(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("\nTidak ada pesanan dalam periode ini.");
            return;
        }
        
        System.out.print("\nTampilkan detail pesanan? (y/n): ");
        String showDetails = scanner.nextLine();
        
        if (!showDetails.toLowerCase().startsWith("y")) {
            return;
        }
        
        System.out.println("\n=== DETAIL PESANAN ===");
        System.out.printf("%-8s %-12s %-15s %-12s %-10s%n", 
                         "Order ID", "Tanggal", "Total", "Pembayaran", "Status");
        System.out.println("=".repeat(70));
        
        for (Order order : orders) {
            System.out.printf("%-8d %-12s Rp%-12.0f %-12s %-10s%n",
                             order.getId(),
                             order.getOrderDate().toLocalDateTime().toLocalDate().format(dateFormatter),
                             order.getTotalAmount(),
                             order.getPaymentMethod(),
                             order.getStatus());
            
            // Show order items
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                System.out.println("  Items:");
                for (OrderItem item : order.getOrderItems()) {
                    System.out.printf("    - %s x%d = Rp%.0f%n",
                                     item.getMenuItem().getName(),
                                     item.getQuantity(),
                                     item.getSubtotal());
                }
            }
            System.out.println();
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
}
