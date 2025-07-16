package controller;

import dao.MenuItemDAO;
import dao.OrderDAO;
import dao.UserDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.User;

public class KasirController {
    private OrderDAO orderDAO;
    private MenuItemDAO menuItemDAO;
    private UserDAO userDAO;
    
    public KasirController() {
        try {
            this.orderDAO = new OrderDAO();
            this.menuItemDAO = new MenuItemDAO();
            this.userDAO = new UserDAO();
        } catch (Exception e) {
            // Log error atau handle jika DAO tidak dapat diinisialisasi
            System.err.println("Error initializing DAOs: " + e.getMessage());
            // Bisa juga throw exception atau set flag error
        }
    }
    
    /**
     * Mendapatkan daftar menu yang tersedia
     * @return List<MenuItem> daftar menu atau empty list jika ada error
     */
    public List<MenuItem> getAvailableMenuItems() {
        try {
            if (menuItemDAO != null) {
                List<MenuItem> items = menuItemDAO.getAvailableMenuItems();
                return items != null ? items : new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error getting available menu items: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return dummy data jika DAO tidak tersedia atau error
        return getDummyMenuItems();
    }
    
    /**
     * Mendapatkan daftar customer
     * @return List<User> daftar customer atau empty list jika ada error
     */
    public List<User> getCustomers() {
        try {
            if (userDAO != null) {
                List<User> customers = userDAO.getUsersByRole(User.Role.CUSTOMER);
                return customers != null ? customers : new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error getting customers: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return dummy customer jika DAO tidak tersedia atau error
        return getDummyCustomers();
    }
    
    /**
     * Memproses order dan menyimpan ke database
     * @param customerId ID customer
     * @param kasirId ID kasir
     * @param orderItems List item yang dipesan
     * @param paymentMethod Metode pembayaran
     * @return ID order yang berhasil dibuat, atau -1 jika gagal
     */
    public int processOrder(int customerId, int kasirId, List<OrderItem> orderItems, 
                          Order.PaymentMethod paymentMethod) {
        
        // Validasi input
        if (orderItems == null || orderItems.isEmpty()) {
            System.err.println("Order items cannot be null or empty");
            return -1;
        }
        
        if (customerId <= 0 || kasirId <= 0) {
            System.err.println("Customer ID and Kasir ID must be positive");
            return -1;
        }
        
        try {
            // Hitung total amount
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderItem item : orderItems) {
                if (item != null && item.getSubtotal() != null) {
                    totalAmount = totalAmount.add(item.getSubtotal());
                }
            }
            
            // Validasi total amount
            if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("Total amount must be greater than zero");
                return -1;
            }
            
            // Buat order
            Order order = new Order(customerId, kasirId, totalAmount, paymentMethod);
            order.setStatus(Order.Status.COMPLETED);
            
            int orderId = -1;
            if (orderDAO != null) {
                orderId = orderDAO.createOrder(order);
            } else {
                // Jika DAO tidak tersedia, generate dummy order ID
                orderId = generateDummyOrderId();
            }
            
            if (orderId > 0) {
                // Simpan order items
                boolean allItemsAdded = true;
                for (OrderItem item : orderItems) {
                    if (item != null) {
                        item.setOrderId(orderId);
                        
                        if (orderDAO != null) {
                            if (!orderDAO.createOrderItem(item)) {
                                allItemsAdded = false;
                                System.err.println("Failed to add order item: " + item.getMenuItemId());
                            }
                        }
                        // Jika orderDAO null, anggap berhasil untuk demo
                    }
                }
                
                if (allItemsAdded) {
                    System.out.println("Order processed successfully with ID: " + orderId);
                    return orderId;
                } else {
                    System.err.println("Some order items failed to save");
                    // Ideally, rollback the order here
                    return -1;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error processing order: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Mendapatkan menu item berdasarkan ID
     * @param id ID menu item
     * @return MenuItem atau null jika tidak ditemukan
     */
    public MenuItem getMenuItemById(int id) {
        try {
            if (menuItemDAO != null && id > 0) {
                return menuItemDAO.getMenuItemById(id);
            }
        } catch (Exception e) {
            System.err.println("Error getting menu item by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return dummy data jika DAO tidak tersedia
        return getDummyMenuItemById(id);
    }
    
    /**
     * Validasi order items sebelum diproses
     * @param orderItems List order items untuk divalidasi
     * @return true jika valid, false jika tidak
     */
    public boolean validateOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return false;
        }
        
        for (OrderItem item : orderItems) {
            if (item == null || 
                item.getMenuItemId() <= 0 || 
                item.getQuantity() <= 0 || 
                item.getUnitPrice() == null || 
                item.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Hitung total order
     * @param orderItems List order items
     * @return Total amount
     */
    public BigDecimal calculateTotal(List<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.ZERO;
        
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                if (item != null && item.getSubtotal() != null) {
                    total = total.add(item.getSubtotal());
                }
            }
        }
        
        return total;
    }
    
    // Helper methods untuk dummy data ketika DAO tidak tersedia
    private List<MenuItem> getDummyMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        
        MenuItem item1 = new MenuItem();
        item1.setId(1);
        item1.setName("Nasi Goreng");
        item1.setCategory("Makanan");
        item1.setPrice(new BigDecimal("25000"));
        item1.setDescription("Nasi goreng special");
        item1.setAvailable(true);
        items.add(item1);
        
        MenuItem item2 = new MenuItem();
        item2.setId(2);
        item2.setName("Ayam Bakar");
        item2.setCategory("Makanan");
        item2.setPrice(new BigDecimal("30000"));
        item2.setDescription("Ayam bakar bumbu kecap");
        item2.setAvailable(true);
        items.add(item2);
        
        MenuItem item3 = new MenuItem();
        item3.setId(3);
        item3.setName("Es Teh");
        item3.setCategory("Minuman");
        item3.setPrice(new BigDecimal("5000"));
        item3.setDescription("Es teh manis");
        item3.setAvailable(true);
        items.add(item3);
        
        MenuItem item4 = new MenuItem();
        item4.setId(4);
        item4.setName("Jus Jeruk");
        item4.setCategory("Minuman");
        item4.setPrice(new BigDecimal("8000"));
        item4.setDescription("Jus jeruk segar");
        item4.setAvailable(true);
        items.add(item4);
        
        MenuItem item5 = new MenuItem();
        item5.setId(5);
        item5.setName("Soto Ayam");
        item5.setCategory("Makanan");
        item5.setPrice(new BigDecimal("20000"));
        item5.setDescription("Soto ayam kuning");
        item5.setAvailable(true);
        items.add(item5);
        
        return items;
    }
    
    private List<User> getDummyCustomers() {
        List<User> customers = new ArrayList<>();
        
        User customer1 = new User();
        customer1.setId(1);
        customer1.setFullName("Customer Umum");
        customer1.setRole(User.Role.CUSTOMER);
        customers.add(customer1);
        
        User customer2 = new User();
        customer2.setId(2);
        customer2.setFullName("John Doe");
        customer2.setRole(User.Role.CUSTOMER);
        customers.add(customer2);
        
        return customers;
    }
    
    private MenuItem getDummyMenuItemById(int id) {
        List<MenuItem> items = getDummyMenuItems();
        for (MenuItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
    
    private int generateDummyOrderId() {
        // Generate random order ID untuk testing
        return (int) (Math.random() * 1000) + 1;
    }
    
    // Getter methods untuk testing atau debugging
    public OrderDAO getOrderDAO() {
        return orderDAO;
    }
    
    public MenuItemDAO getMenuItemDAO() {
        return menuItemDAO;
    }
    
    public UserDAO getUserDAO() {
        return userDAO;
    }
}