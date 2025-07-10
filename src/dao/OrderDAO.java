package dao;

import model.Order;
import model.OrderItem;
import model.MenuItem;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection connection;
    
    public OrderDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    public int createOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id, kasir_id, total_amount, payment_method, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setInt(2, order.getKasirId());
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setString(4, order.getPaymentMethod().name());
            stmt.setString(5, order.getStatus().name());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Create order error: " + e.getMessage());
        }
        return -1;
    }
    
    public boolean createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getMenuItemId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setBigDecimal(4, orderItem.getUnitPrice());
            stmt.setBigDecimal(5, orderItem.getSubtotal());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Create order item error: " + e.getMessage());
            return false;
        }
    }
    
    public List<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE DATE(order_date) BETWEEN ? AND ? ORDER BY order_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(getOrderItemsByOrderId(order.getId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Get orders by date range error: " + e.getMessage());
        }
        return orders;
    }
    
    public BigDecimal getTotalSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM orders WHERE DATE(order_date) BETWEEN ? AND ? AND status = 'COMPLETED'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            System.err.println("Get total sales error: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    public int getOrderCountByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) as count FROM orders WHERE DATE(order_date) BETWEEN ? AND ? AND status = 'COMPLETED'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Get order count error: " + e.getMessage());
        }
        return 0;
    }
    
    private List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT oi.*, mi.name, mi.description FROM order_items oi " +
                    "JOIN menu_items mi ON oi.menu_item_id = mi.id WHERE oi.order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(rs.getInt("id"));
                orderItem.setOrderId(rs.getInt("order_id"));
                orderItem.setMenuItemId(rs.getInt("menu_item_id"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setUnitPrice(rs.getBigDecimal("unit_price"));
                orderItem.setSubtotal(rs.getBigDecimal("subtotal"));
                
                MenuItem menuItem = new MenuItem();
                menuItem.setId(rs.getInt("menu_item_id"));
                menuItem.setName(rs.getString("name"));
                menuItem.setDescription(rs.getString("description"));
                orderItem.setMenuItem(menuItem);
                
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            System.err.println("Get order items error: " + e.getMessage());
        }
        return orderItems;
    }
    
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setKasirId(rs.getInt("kasir_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setPaymentMethod(Order.PaymentMethod.valueOf(rs.getString("payment_method")));
        order.setStatus(Order.Status.valueOf(rs.getString("status")));
        order.setOrderDate(rs.getTimestamp("order_date"));
        return order;
    }
}
