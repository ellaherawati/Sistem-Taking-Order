package controller;

import dao.MenuItemDAO;
import dao.OrderDAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import model.MenuItem;
import model.Order;
import model.OrderItem;

public class CustomerController {
    private MenuItemDAO menuItemDAO;
    
    public CustomerController() {
        this.menuItemDAO = new MenuItemDAO();
    }
    
    public List<MenuItem> viewMenu() {
        return menuItemDAO.getAvailableMenuItems();
    }
    
    public List<MenuItem> getMenuByCategory(String category) {
        return menuItemDAO.getAvailableMenuItems()
                .stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
    
    public List<String> getAvailableCategories() {
        return menuItemDAO.getAvailableMenuItems()
                .stream()
                .map(MenuItem::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean createOrder(int customerId, List<OrderItem> orderItems, Order.PaymentMethod paymentMethod) {
        OrderDAO orderDAO = new OrderDAO();

        // Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            totalAmount = totalAmount.add(item.getSubtotal());
        }

        // Create order (customer acts as their own kasir)
        Order order = new Order(customerId, customerId, totalAmount, paymentMethod);
        order.setStatus(Order.Status.COMPLETED);
        int orderId = orderDAO.createOrder(order);

        if (orderId > 0) {
            // Add order items
            boolean allItemsAdded = true;
            for (OrderItem item : orderItems) {
                item.setOrderId(orderId);
                if (!orderDAO.createOrderItem(item)) {
                    allItemsAdded = false;
                }
            }

            return allItemsAdded;
        }

        return false;
    }

    public MenuItem getMenuItemById(int id) {
        return menuItemDAO.getMenuItemById(id);
    }
}
