package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private int kasirId;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private Status status;
    private Timestamp orderDate;
    private List<OrderItem> orderItems;
    
    public enum PaymentMethod {
        CASH, QRIS
    }
    
    public enum Status {
        PENDING, COMPLETED, CANCELLED
    }
    
    // Constructors
    public Order() {}
    
    public Order(int customerId, int kasirId, BigDecimal totalAmount, PaymentMethod paymentMethod) {
        this.customerId = customerId;
        this.kasirId = kasirId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = Status.PENDING;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getKasirId() { return kasirId; }
    public void setKasirId(int kasirId) { this.kasirId = kasirId; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}
