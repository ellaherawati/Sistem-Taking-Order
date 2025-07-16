package controller;

import dao.MenuItemDAO;
import dao.UserDAO;
import java.util.List;
import model.MenuItem;
import model.User;

public class AdminController {
    private UserDAO userDAO;
    private MenuItemDAO menuItemDAO;
    
    public AdminController() {
        this.userDAO = new UserDAO();
        this.menuItemDAO = new MenuItemDAO();
    }
    
    public boolean createEmployee(User user) {
        return userDAO.createUser(user);
    }

    public boolean createEmployee(String username, String password, User.Role role, String fullName, String email, String phone) {
        User user = new User();
        user.setPhone(phone);
        return userDAO.createUser(user);
    }
    
    public List<User> getAllEmployees() {
        return userDAO.getAllUsers();
    }

    public List<User> getEmployeesByRole(User.Role role) {
        return userDAO.getUsersByRole(role);
    }
    
    public boolean addMenuItem(MenuItem menuItem) {
        return menuItemDAO.createMenuItem(menuItem);
    }
    
    public boolean updateMenuItem(MenuItem menuItem) {
        return menuItemDAO.updateMenuItem(menuItem);
    }
    
    public boolean deleteMenuItem(int menuItemId) {
        return menuItemDAO.deleteMenuItem(menuItemId);
    }
    
    public List<MenuItem> getAllMenuItems() {
        return menuItemDAO.getAllMenuItems();
    }
    
    public MenuItem getMenuItemById(int id) {
        return menuItemDAO.getMenuItemById(id);
    }
}
