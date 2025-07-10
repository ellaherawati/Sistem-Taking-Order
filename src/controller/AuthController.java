package controller;

import dao.UserDAO;
import model.User;

public class AuthController {
    private UserDAO userDAO;
    private User currentUser;
    
    public AuthController() {
        this.userDAO = new UserDAO();
    }
    
    public boolean login(String username, String password) {
        User user = userDAO.authenticate(username, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean hasRole(User.Role role) {
        return currentUser != null && currentUser.getRole() == role;
    }
}
