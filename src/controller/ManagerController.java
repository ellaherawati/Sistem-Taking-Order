package controller;

import dao.OrderDAO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import model.Order;

public class ManagerController {
    private OrderDAO orderDAO;
    
    public ManagerController() {
        this.orderDAO = new OrderDAO();
    }
    
    public List<Order> getDailySalesReport(LocalDate date) {
        return orderDAO.getOrdersByDateRange(date, date);
    }
    
    public BigDecimal getDailySalesTotal(LocalDate date) {
        return orderDAO.getTotalSalesByDateRange(date, date);
    }
    
    public int getDailyOrderCount(LocalDate date) {
        return orderDAO.getOrderCountByDateRange(date, date);
    }
    
    public List<Order> getWeeklySalesReport(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate startOfWeek = date.with(weekFields.dayOfWeek(), 1);
        LocalDate endOfWeek = date.with(weekFields.dayOfWeek(), 7);
        
        return orderDAO.getOrdersByDateRange(startOfWeek, endOfWeek);
    }
    
    public BigDecimal getWeeklySalesTotal(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate startOfWeek = date.with(weekFields.dayOfWeek(), 1);
        LocalDate endOfWeek = date.with(weekFields.dayOfWeek(), 7);
        
        return orderDAO.getTotalSalesByDateRange(startOfWeek, endOfWeek);
    }
    
    public List<Order> getMonthlySalesReport(LocalDate date) {
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        
        return orderDAO.getOrdersByDateRange(startOfMonth, endOfMonth);
    }
    
    public BigDecimal getMonthlySalesTotal(LocalDate date) {
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        
        return orderDAO.getTotalSalesByDateRange(startOfMonth, endOfMonth);
    }
    
    public List<Order> getCustomSalesReport(LocalDate startDate, LocalDate endDate) {
        return orderDAO.getOrdersByDateRange(startDate, endDate);
    }
    
    public BigDecimal getCustomSalesTotal(LocalDate startDate, LocalDate endDate) {
        return orderDAO.getTotalSalesByDateRange(startDate, endDate);
    }

    public int getWeeklyOrderCount(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate startOfWeek = date.with(weekFields.dayOfWeek(), 1);
        LocalDate endOfWeek = date.with(weekFields.dayOfWeek(), 7);
        
        return orderDAO.getOrderCountByDateRange(startOfWeek, endOfWeek);
    }

    public int getMonthlyOrderCount(LocalDate date) {
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        
        return orderDAO.getOrderCountByDateRange(startOfMonth, endOfMonth);
    }

    public int getCustomOrderCount(LocalDate startDate, LocalDate endDate) {
        return orderDAO.getOrderCountByDateRange(startDate, endDate);
    }
}
