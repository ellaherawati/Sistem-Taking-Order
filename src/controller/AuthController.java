package controller;

import java.util.HashMap;
import java.util.Map;
import model.User;

public class AuthController {
    private User currentUser;
    private Map<String, User> users;
    
    public AuthController() {
        // Inisialisasi data user dummy
        users = new HashMap<>();
        
        // Tambahkan user dummy untuk testing
        User admin = new User();
        admin.setId(1);
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setFullName("Administrator");
        admin.setRole("ADMIN");
        users.put("admin", admin);
        
        User kasir = new User();
        kasir.setId(2);
        kasir.setUsername("kasir");
        kasir.setPassword("kasir123");
        kasir.setFullName("Kasir 1");
        kasir.setRole("CASHIER");
        users.put("kasir", kasir);
    }
    
    public boolean login(String username, String password) {
        System.out.println("Attempting login with username: " + username);
        
        if (username == null || password == null) {
            System.out.println("Username or password is null");
            return false;
        }
        
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            this.currentUser = user;
            System.out.println("Login successful for user: " + username);
            return true;
        }
        
        System.out.println("Login failed for user: " + username);
        return false;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void logout() {
        this.currentUser = null;
        System.out.println("User logged out");
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}