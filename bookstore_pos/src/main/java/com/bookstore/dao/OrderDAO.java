package com.bookstore.dao;

import com.bookstore.model.Order;
import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.Customer;
import com.bookstore.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class OrderDAO {
    private CustomerDAO customerDAO = new CustomerDAO();
    private UserDAO userDAO = new UserDAO();

    public int addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (date, customer_id, employee_id, total) VALUES (?, ?, ?, ?)";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setObject(1, order.getDate()); // Lưu LocalDateTime
            if (order.getCustomer() != null) {
                stmt.setInt(2, order.getCustomer().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, order.getEmployee().getId());
            stmt.setDouble(4, order.getTotal());
            stmt.executeUpdate();

            // Lấy ID của đơn hàng vừa tạo
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    order.setId(orderId);
                    return orderId;
                }
            }
        }
        throw new SQLException("Không thể lấy ID của đơn hàng vừa tạo!");
    }

    public ArrayList<Order> getAllOrders() throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DataBaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setDate(rs.getObject("date", LocalDateTime.class));
                order.setTotal(rs.getDouble("total"));

                int customerId = rs.getInt("customer_id");
                if (!rs.wasNull()) {
                    Customer customer = customerDAO.getAllCustomers().stream()
                            .filter(c -> c.getId() == customerId)
                            .findFirst()
                            .orElse(null);
                    order.setCustomer(customer);
                }

                int employeeId = rs.getInt("employee_id");
                if (!rs.wasNull()) {
                    User employee = userDAO.getAllUsers().stream()
                            .filter(u -> u.getId() == employeeId)
                            .findFirst()
                            .orElse(null);
                    order.setEmployee(employee);
                }

                orders.add(order);
            }
        }
        return orders;
    }

    public ArrayList<Order> searchOrders(Map<String, String> criteria) throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT o.* FROM orders o LEFT JOIN customers c ON o.customer_id = c.id WHERE 1=1");
        ArrayList<String> params = new ArrayList<>();

        if (criteria.containsKey("id")) {
            sql.append(" AND o.id = ?");
            params.add(criteria.get("id"));
        }
        if (criteria.containsKey("customer_name")) {
            sql.append(" AND c.name LIKE ?");
            params.add("%" + criteria.get("customer_name") + "%");
        }
        if (criteria.containsKey("date")) {
            sql.append(" AND DATE(o.date) = ?");
            params.add(criteria.get("date"));
        }
        if (criteria.containsKey("total_min")) {
            sql.append(" AND o.total >= ?");
            params.add(criteria.get("total_min"));
        }
        if (criteria.containsKey("total_max")) {
            sql.append(" AND o.total <= ?");
            params.add(criteria.get("total_max"));
        }
        if (criteria.containsKey("employee_id")) {
            sql.append(" AND o.employee_id = ?");
            params.add(criteria.get("employee_id"));
        }

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setDate(rs.getObject("date", LocalDateTime.class));
                    order.setTotal(rs.getDouble("total"));

                    int customerId = rs.getInt("customer_id");
                    if (!rs.wasNull()) {
                        Customer customer = customerDAO.getAllCustomers().stream()
                                .filter(c -> c.getId() == customerId)
                                .findFirst()
                                .orElse(null);
                        order.setCustomer(customer);
                    }

                    int employeeId = rs.getInt("employee_id");
                    if (!rs.wasNull()) {
                        User employee = userDAO.getAllUsers().stream()
                                .filter(u -> u.getId() == employeeId)
                                .findFirst()
                                .orElse(null);
                        order.setEmployee(employee);
                    }

                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public Order getOrderById(int id) throws SQLException{
        String sql = "SELECT * FROM orders WHERE id =  ?";
        try (Connection conn = DataBaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
                    stmt.setInt(1, id);
                    if (rs.next()) {
                        Order order = new Order();
                        order.setId(rs.getInt("id"));
                        order.setDate(rs.getObject("date", LocalDateTime.class));
                        order.setTotal(rs.getDouble("total"));

                        int customerId = rs.getInt("customer_id");
                        if (!rs.wasNull()) {
                            Customer customer = customerDAO.getAllCustomers().stream()
                                .filter(c -> c.getId() == customerId)
                                .findFirst()
                                .orElse(null);
                            order.setCustomer(customer);
                        }

                    int employeeId = rs.getInt("employee_id");
                    if (!rs.wasNull()) {
                        User employee = userDAO.getAllUsers().stream()
                            .filter(u -> u.getId() == employeeId)
                            .findFirst()
                            .orElse(null);
                        order.setEmployee(employee);
                    }
            }
        }
        return null;
    }
}