package view;

import controller.AuthController;
import controller.AdminController;
import model.User;
import model.MenuItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class AdminView {
    private Scanner scanner;
    private AuthController authController;
    private AdminController adminController;
    
    public AdminView(AuthController authController) {
        this.scanner = new Scanner(System.in);
        this.authController = authController;
        this.adminController = new AdminController();
    }
    
    public void showDashboard() {
        while (authController.isLoggedIn()) {
            System.out.println("\n=================================");
            System.out.println("        DASHBOARD ADMIN");
            System.out.println("=================================");
            System.out.println("1. Kelola Karyawan");
            System.out.println("2. Kelola Menu");
            System.out.println("3. Logout");
            System.out.print("Pilih menu: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    showEmployeeManagement();
                    break;
                case 2:
                    showMenuManagement();
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
    
    private void showEmployeeManagement() {
        while (true) {
            System.out.println("\n=== KELOLA KARYAWAN ===");
            System.out.println("1. Tambah Karyawan Baru");
            System.out.println("2. Lihat Semua Karyawan");
            System.out.println("3. Lihat Karyawan per Role");
            System.out.println("4. Kembali");
            System.out.print("Pilih menu: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addNewEmployee();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    viewEmployeesByRole();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    private void addNewEmployee() {
        System.out.println("\n=== TAMBAH KARYAWAN BARU ===");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Nama Lengkap: ");
        String fullName = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Telepon: ");
        String phone = scanner.nextLine();
        
        System.out.println("Role:");
        System.out.println("1. KASIR");
        System.out.println("2. ADMIN");
        System.out.println("3. MANAGER");
        System.out.println("4. CUSTOMER");
        System.out.print("Pilih role: ");
        
        int roleChoice = getIntInput();
        User.Role role;
        
        switch (roleChoice) {
            case 1: role = User.Role.KASIR; break;
            case 2: role = User.Role.ADMIN; break;
            case 3: role = User.Role.MANAGER; break;
            case 4: role = User.Role.CUSTOMER; break;
            default:
                System.out.println("Role tidak valid!");
                return;
        }
        
        if (adminController.createEmployee(username, password, role, fullName, email, phone)) {
            System.out.println("Karyawan berhasil ditambahkan!");
        } else {
            System.out.println("Gagal menambahkan karyawan!");
        }
    }
    
    private void viewAllEmployees() {
        System.out.println("\n=== DAFTAR SEMUA KARYAWAN ===");
        List<User> employees = adminController.getAllEmployees();
        
        if (employees.isEmpty()) {
            System.out.println("Tidak ada karyawan.");
            return;
        }
        
        System.out.printf("%-5s %-15s %-10s %-25s %-25s%n", 
                         "ID", "Username", "Role", "Nama Lengkap", "Email");
        System.out.println("=".repeat(80));
        
        for (User employee : employees) {
            System.out.printf("%-5d %-15s %-10s %-25s %-25s%n",
                             employee.getId(),
                             employee.getUsername(),
                             employee.getRole(),
                             employee.getFullName(),
                             employee.getEmail());
        }
    }
    
    private void viewEmployeesByRole() {
        System.out.println("\n=== LIHAT KARYAWAN PER ROLE ===");
        System.out.println("1. KASIR");
        System.out.println("2. ADMIN");
        System.out.println("3. MANAGER");
        System.out.println("4. CUSTOMER");
        System.out.print("Pilih role: ");
        
        int roleChoice = getIntInput();
        User.Role role;
        
        switch (roleChoice) {
            case 1: role = User.Role.KASIR; break;
            case 2: role = User.Role.ADMIN; break;
            case 3: role = User.Role.MANAGER; break;
            case 4: role = User.Role.CUSTOMER; break;
            default:
                System.out.println("Role tidak valid!");
                return;
        }
        
        List<User> employees = adminController.getEmployeesByRole(role);
        
        if (employees.isEmpty()) {
            System.out.println("Tidak ada karyawan dengan role " + role);
            return;
        }
        
        System.out.printf("%-5s %-15s %-25s %-25s%n", 
                         "ID", "Username", "Nama Lengkap", "Email");
        System.out.println("=".repeat(70));
        
        for (User employee : employees) {
            System.out.printf("%-5d %-15s %-25s %-25s%n",
                             employee.getId(),
                             employee.getUsername(),
                             employee.getFullName(),
                             employee.getEmail());
        }
    }
    
    private void showMenuManagement() {
        while (true) {
            System.out.println("\n=== KELOLA MENU ===");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Lihat Semua Menu");
            System.out.println("3. Update Menu");
            System.out.println("4. Hapus Menu");
            System.out.println("5. Kembali");
            System.out.print("Pilih menu: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addNewMenuItem();
                    break;
                case 2:
                    viewAllMenuItems();
                    break;
                case 3:
                    updateMenuItem();
                    break;
                case 4:
                    deleteMenuItem();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    private void addNewMenuItem() {
        System.out.println("\n=== TAMBAH MENU BARU ===");
        
        System.out.print("Nama Menu: ");
        String name = scanner.nextLine();
        
        System.out.print("Deskripsi: ");
        String description = scanner.nextLine();
        
        System.out.print("Harga: ");
        BigDecimal price = getBigDecimalInput();
        
        System.out.print("Kategori: ");
        String category = scanner.nextLine();
        
        System.out.print("Tersedia (y/n): ");
        boolean isAvailable = scanner.nextLine().toLowerCase().startsWith("y");
        
        MenuItem menuItem = new MenuItem(name, description, price, category);
        menuItem.setAvailable(isAvailable);
        
        if (adminController.addMenuItem(menuItem)) {
            System.out.println("Menu berhasil ditambahkan!");
        } else {
            System.out.println("Gagal menambahkan menu!");
        }
    }
    
    private void viewAllMenuItems() {
        System.out.println("\n=== DAFTAR SEMUA MENU ===");
        List<MenuItem> menuItems = adminController.getAllMenuItems();
        
        if (menuItems.isEmpty()) {
            System.out.println("Tidak ada menu.");
            return;
        }
        
        System.out.printf("%-5s %-20s %-15s %-15s %-10s%n", 
                         "ID", "Nama", "Kategori", "Harga", "Tersedia");
        System.out.println("=".repeat(70));
        
        for (MenuItem item : menuItems) {
            System.out.printf("%-5d %-20s %-15s Rp%-12.0f %-10s%n",
                             item.getId(),
                             item.getName(),
                             item.getCategory(),
                             item.getPrice(),
                             item.isAvailable() ? "Ya" : "Tidak");
        }
    }
    
    private void updateMenuItem() {
        System.out.println("\n=== UPDATE MENU ===");
        viewAllMenuItems();
        
        System.out.print("Masukkan ID menu yang akan diupdate: ");
        int id = getIntInput();
        
        MenuItem menuItem = adminController.getMenuItemById(id);
        if (menuItem == null) {
            System.out.println("Menu tidak ditemukan!");
            return;
        }
        
        System.out.println("Data saat ini:");
        System.out.println("Nama: " + menuItem.getName());
        System.out.println("Deskripsi: " + menuItem.getDescription());
        System.out.println("Harga: " + menuItem.getPrice());
        System.out.println("Kategori: " + menuItem.getCategory());
        System.out.println("Tersedia: " + (menuItem.isAvailable() ? "Ya" : "Tidak"));
        
        System.out.print("\nNama baru (kosong untuk tidak mengubah): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            menuItem.setName(name);
        }
        
        System.out.print("Deskripsi baru (kosong untuk tidak mengubah): ");
        String description = scanner.nextLine();
        if (!description.trim().isEmpty()) {
            menuItem.setDescription(description);
        }
        
        System.out.print("Harga baru (0 untuk tidak mengubah): ");
        BigDecimal price = getBigDecimalInput();
        if (price.compareTo(BigDecimal.ZERO) > 0) {
            menuItem.setPrice(price);
        }
        
        System.out.print("Kategori baru (kosong untuk tidak mengubah): ");
        String category = scanner.nextLine();
        if (!category.trim().isEmpty()) {
            menuItem.setCategory(category);
        }
        
        System.out.print("Tersedia (y/n/kosong untuk tidak mengubah): ");
        String available = scanner.nextLine();
        if (!available.trim().isEmpty()) {
            menuItem.setAvailable(available.toLowerCase().startsWith("y"));
        }
        
        if (adminController.updateMenuItem(menuItem)) {
            System.out.println("Menu berhasil diupdate!");
        } else {
            System.out.println("Gagal mengupdate menu!");
        }
    }
    
    private void deleteMenuItem() {
        System.out.println("\n=== HAPUS MENU ===");
        viewAllMenuItems();
        
        System.out.print("Masukkan ID menu yang akan dihapus: ");
        int id = getIntInput();
        
        MenuItem menuItem = adminController.getMenuItemById(id);
        if (menuItem == null) {
            System.out.println("Menu tidak ditemukan!");
            return;
        }
        
        System.out.println("Menu yang akan dihapus: " + menuItem.getName());
        System.out.print("Yakin ingin menghapus? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.toLowerCase().startsWith("y")) {
            if (adminController.deleteMenuItem(id)) {
                System.out.println("Menu berhasil dihapus!");
            } else {
                System.out.println("Gagal menghapus menu!");
            }
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }
    
    private int getIntInput() {
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
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
