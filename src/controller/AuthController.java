package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import model.User;

public class AuthController {

    private Map<String, User> users;
    private Map<String, User> activeSessions;

    public AuthController() {
        // Inisialisasi user contoh, bisa diubah ke database
        users = new HashMap<>();
        users.put("admin", new User(1, "admin", "adminpass", "ADMIN", null));
        users.put("kasir", new User(2, "kasir", "kasirpass", "CASHIER", null));
        users.put("manager", new User(3, "manager", "managerpass", "MANAGER", null));
        // Tambahkan user lain sesuai kebutuhan

        activeSessions = new HashMap<>();
    }

    /**
     * Fungsi login untuk staff (Admin, Kasir, Manager).
     * Jika username dan password cocok, buat sessionId unik dan simpan di activeSessions.
     * @return sessionId jika sukses, null jika gagal
     */
    public String login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            // Generate sessionId unik dengan UUID
            String sessionId = UUID.randomUUID().toString();
            activeSessions.put(sessionId, user);
            return sessionId;
        }
        return null;
    }

    /**
     * Fungsi untuk customer yang masuk tanpa login.
     * Membuat temporary session untuk customer.
     * @return sessionId untuk customer
     */
    public String loginAsCustomer() {
        // Buat user customer temporary
        User customerUser = new User(0, "customer", "", "CUSTOMER", null);
        String sessionId = UUID.randomUUID().toString();
        activeSessions.put(sessionId, customerUser);
        return sessionId;
    }

    /**
     * Logout user berdasarkan sessionId.
     */
    public void logout(String sessionId) {
        if (sessionId != null) {
            activeSessions.remove(sessionId);
        }
    }

    /**
     * Mengecek apakah sessionId valid (user masih login).
     */
    public boolean isLoggedIn(String sessionId) {
        return sessionId != null && activeSessions.containsKey(sessionId);
    }

    /**
     * Mengambil User yang login berdasarkan sessionId.
     */
    public User getCurrentUserBySession(String sessionId) {
        if (isLoggedIn(sessionId)) {
            return activeSessions.get(sessionId);
        }
        return null;
    }

    /**
     * Mengembalikan halaman dashboard sesuai role user berdasarkan sessionId.
     * Jika session tidak valid, kembalikan "CUSTOMER_DASHBOARD" sebagai default.
     */
    public String getDashboardPage(String sessionId) {
        User user = getCurrentUserBySession(sessionId);
        if (user == null) {
            return "CUSTOMER_DASHBOARD";
        }
        String role = user.getRole();
        switch (role) {
            case "ADMIN":
                return "ADMIN_DASHBOARD";
            case "CASHIER":
                return "CASHIER_DASHBOARD";
            case "MANAGER":
                return "MANAGER_DASHBOARD";
            case "CUSTOMER":
                return "CUSTOMER_DASHBOARD";
            default:
                return "CUSTOMER_DASHBOARD";
        }
    }

    /**
     * Mendapatkan role user berdasarkan sessionId
     */
    public String getUserRole(String sessionId) {
        User user = getCurrentUserBySession(sessionId);
        return user != null ? user.getRole() : "CUSTOMER";
    }

    /**
     * Mendapatkan username berdasarkan sessionId
     */
    public String getUsername(String sessionId) {
        User user = getCurrentUserBySession(sessionId);
        return user != null ? user.getUsername() : "Customer";
    }

    /**
     * Mengecek apakah user adalah customer
     */
    public boolean isCustomer(String sessionId) {
        return "CUSTOMER".equals(getUserRole(sessionId));
    }

    /**
     * Mengecek apakah user adalah staff (Admin, Kasir, Manager)
     */
    public boolean isStaff(String sessionId) {
        String role = getUserRole(sessionId);
        return "ADMIN".equals(role) || "CASHIER".equals(role) || "MANAGER".equals(role);
    }

    /**
     * Mendapatkan semua user yang terdaftar (untuk testing)
     */
    public Map<String, User> getAllUsers() {
        return new HashMap<>(users);
    }

    /**
     * Mendapatkan semua session yang aktif (untuk monitoring)
     */
    public Map<String, User> getActiveSessions() {
        return new HashMap<>(activeSessions);
    }
}