package dao;

import model.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    private Connection connection;
    
    public MenuItemDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    public boolean createMenuItem(MenuItem menuItem) {
        String sql = "INSERT INTO menu_items (name, description, price, category, is_available, image_url) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, menuItem.getName());
            stmt.setString(2, menuItem.getDescription());
            stmt.setBigDecimal(3, menuItem.getPrice());
            stmt.setString(4, menuItem.getCategory());
            stmt.setBoolean(5, menuItem.isAvailable());
            stmt.setString(6, menuItem.getImageUrl());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Create menu item error: " + e.getMessage());
            return false;
        }
    }
    
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu_items ORDER BY category, name";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                menuItems.add(mapResultSetToMenuItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get all menu items error: " + e.getMessage());
        }
        return menuItems;
    }
    
    public List<MenuItem> getAvailableMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE is_available = TRUE ORDER BY category, name";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                menuItems.add(mapResultSetToMenuItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get available menu items error: " + e.getMessage());
        }
        return menuItems;
    }
    
    public MenuItem getMenuItemById(int id) {
        String sql = "SELECT * FROM menu_items WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMenuItem(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get menu item by ID error: " + e.getMessage());
        }
        return null;
    }
    
    public boolean updateMenuItem(MenuItem menuItem) {
        String sql = "UPDATE menu_items SET name = ?, description = ?, price = ?, category = ?, is_available = ?, image_url = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, menuItem.getName());
            stmt.setString(2, menuItem.getDescription());
            stmt.setBigDecimal(3, menuItem.getPrice());
            stmt.setString(4, menuItem.getCategory());
            stmt.setBoolean(5, menuItem.isAvailable());
            stmt.setString(6, menuItem.getImageUrl());
            stmt.setInt(7, menuItem.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update menu item error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteMenuItem(int id) {
        String sql = "DELETE FROM menu_items WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete menu item error: " + e.getMessage());
            return false;
        }
    }
    
    private MenuItem mapResultSetToMenuItem(ResultSet rs) throws SQLException {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(rs.getInt("id"));
        menuItem.setName(rs.getString("name"));
        menuItem.setDescription(rs.getString("description"));
        menuItem.setPrice(rs.getBigDecimal("price"));
        menuItem.setCategory(rs.getString("category"));
        menuItem.setAvailable(rs.getBoolean("is_available"));
        menuItem.setImageUrl(rs.getString("image_url"));
        menuItem.setCreatedAt(rs.getTimestamp("created_at"));
        menuItem.setUpdatedAt(rs.getTimestamp("updated_at"));
        return menuItem;
    }
}
