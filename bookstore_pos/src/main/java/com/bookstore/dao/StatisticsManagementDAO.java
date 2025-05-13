package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.StatisticsManagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticsManagementDAO {
    private Connection connection;

    public StatisticsManagementDAO() throws SQLException {
        this.connection = DataBaseConfig.getConnection();
    }

    public List<StatisticsManagement> getAll() throws SQLException {
        List<StatisticsManagement> statisticsManagements = new ArrayList<>();
        String sql = "SELECT * FROM statistics_management";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StatisticsManagement statisticsManagement = new StatisticsManagement(
                    rs.getInt("id"),
                    rs.getBoolean("can_view")
                );
                statisticsManagements.add(statisticsManagement);
            }
        }
        return statisticsManagements;
    }

    public StatisticsManagement getById(int id) throws SQLException {
        String sql = "SELECT * FROM statistics_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new StatisticsManagement(
                        rs.getInt("id"),
                        rs.getBoolean("can_view")
                    );
                }
            }
        }
        return null;
    }

    public boolean add(StatisticsManagement statisticsManagement) throws SQLException {
        String sql = "INSERT INTO statistics_management (id, can_view) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, statisticsManagement.getId());
            pstmt.setBoolean(2, statisticsManagement.isCanView());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean update(StatisticsManagement statisticsManagement) throws SQLException {
        String sql = "UPDATE statistics_management SET can_view = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, statisticsManagement.isCanView());
            pstmt.setInt(2, statisticsManagement.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM statistics_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}