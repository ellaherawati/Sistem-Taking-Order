package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String email;
    private String phone;
    private boolean active;
    
    // Constructor
    public User() {
        this.active = true;
    }
    
    public User(int id, String username, String password, String fullName, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.active = true;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(Role customer) {
        this.role = customer;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public String toString() {
        return fullName != null ? fullName : username;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public static class Role {

        public static final Role CUSTOMER = null;
        public static final Role KASIR = null;
        public static final Role MANAGER = null;
        public static Role MANAGER;

        public Role() {
        }

        public static Object values() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'values'");
        }
    }

    public static class Role {

        public Role() {
        }
    }

    public class Role {
    }
}