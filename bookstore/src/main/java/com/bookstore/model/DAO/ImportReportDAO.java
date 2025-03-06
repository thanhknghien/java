package com.bookstore.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.ImportReportDTO;

public class ImportReportDAO {
    public ArrayList<ImportReportDTO> getImportReports() {
        ArrayList<ImportReportDTO> list = new ArrayList<>();
        String query = " SELECT  i.SlipID, i.ImportDate, e.Name AS EmployeeName, "
                        + " b.title AS BookTitle, d.Quantity, d.UnitPrice," +
                        "(d.Quantity * d.UnitPrice) AS TotalCost" +
                        "FROM ImportSlip i" +
                        "JOIN Employee e ON i.EmployeeID = e.EmployeeID" +
                        "JOIN ImportSlipDetail d ON i.SlipID = d.SlipID" +
                        "JOIN Book b ON d.BookID = b.BookID" +
                        "ORDER BY i.ImportDate DESC;";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    list.add(new ImportReportDTO(
                            rs.getInt("SlipID"),
                            rs.getTimestamp("ImportDate"),
                            rs.getString("EmployeeName"),
                            rs.getString("BookTitle"),
                            rs.getInt("Quantity"),
                            rs.getDouble("UnitPrice"),
                            rs.getDouble("TotalCost")
                    ));
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
