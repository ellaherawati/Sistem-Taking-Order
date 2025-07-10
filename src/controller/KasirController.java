package controller;

import dao.OrderDAO;
import dao.MenuItemDAO;
import dao.UserDAO;
import model.Order;
import model.OrderItem;
import model.MenuItem;
import model.User;
import java.math.BigDecimal;
import java.util.List;

public class KasirController {
    private OrderDAO orderDAO;
    private MenuItemDAO menuItemDAO;
    private UserDAO userDAO;
    
    public KasirController() {
        this.orderDAO = new OrderDAO();
        this.menuItemDAO = new MenuItemDAO();
        this.userDAO = new UserDAO();
    }
    
    public List<MenuItem> getAvailableMenuItems() {
        return menuItemDAO.getAvailableMenuItems();
    }
    
    public List<User> getCustomers() {
        return userDAO.getUsersByRole(User.Role.CUSTOMER);
    }
    
    public int processOrder(int customerId, int kasirId, List<OrderItem> orderItems, 
                          Order.PaymentMethod paymentMethod) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            totalAmount = totalAmount.add(item.getSubtotal());
        }
        
        Order order = new Order(customerId, kasirId, totalAmount, paymentMethod);
        order.setStatus(Order.Status.COMPLETED);
        int orderId = orderDAO.createOrder(order);
        
        if (orderId > 0) {
            boolean allItemsAdded = true;
            for (OrderItem item : orderItems) {
                item.setOrderId(orderId);
                if (!orderDAO.createOrderItem(item)) {
                    allItemsAdded = false;
                }
            }
            
            if (allItemsAdded) {
                return orderId;
            }
        }
        
        return -1;
    }
    
    public MenuItem getMenuItemById(int id) {
        return menuItemDAO.getMenuItemById(id);
    }
}
