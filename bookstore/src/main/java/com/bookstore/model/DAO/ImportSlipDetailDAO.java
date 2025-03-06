package com.bookstore.model.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.ImportSlipDetailDTO;

public class ImportSlipDetailDAO {
    private Connection conn;

    public ImportSlipDetailDAO() throws SQLException {
        conn = DBConnect.getConnection();
    }

}
