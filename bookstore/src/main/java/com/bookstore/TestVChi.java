package com.bookstore;

import com.bookstore.model.DAO.ImportSlipDAO;
import com.bookstore.model.DTO.ImportSlipDTO;
import com.bookstore.model.DBConnect;
import com.bookstore.model.DAO.ImportSlipDetailDAO;
import com.bookstore.model.DAO.SupplierDAO;
import com.bookstore.model.DTO.SupplierDTO;
import com.bookstore.model.DTO.ImportSlipDetailDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Supplier;

import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import;

public class TestVChi {
    public static void main(String[] args) {
        try (Connection conn = DBConnect.getConnection()) {
            // Tắt khóa ngoại để dễ thao tác dữ liệu khi test
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
            // Tạo 1 đối tượng ImportSlipDAO để thao tác với dữ liệu
            // ImportSlipDAO importSlipDAO = new ImportSlipDAO();
            // Tạo đối tượng ImportSlipDTO

            // ImportSlipDTO importSlip = new ImportSlipDTO(11122, 112, 112, Date.valueOf("2021-01-01"), 111.0);
            // importSlipDAO.insertImportSlip(importSlip);

            // Lấy danh sách 
            // ArrayList<ImportSlipDTO> importSlipList = importSlipDAO.getAllImportSlips();
            // for (ImportSlipDTO importSlipL : importSlipList){
            //     System.out.println(importSlipL.toString());
            // }

            //Sua thong tin
            // ImportSlipDTO importSlip = new ImportSlipDTO(1111, 1551, 1112, Date.valueOf("2021-01-01"), 110001.0);
            // importSlipDAO.updateImportSlip(importSlip);

            //Truy van 
            // ImportSlipDTO importSlipDTO = importSlipDAO.getImportSlipById(111111);
            // System.out.println(importSlipDTO.toString());

            // Tạo đối tượng ImportSlipDetailDAO
            ImportSlipDetailDAO i = new ImportSlipDetailDAO();

            // Them doi tuong 
            // ImportSlipDetailDTO detail = new ImportSlipDetailDTO(2223, 2232, 21122, 2222.0);
            // i.insertImportSlipDetail(detail);

            //Lay danh sach
            // ArrayList<ImportSlipDetailDTO> list = i.getAllImportSlipDetails();
            // for (ImportSlipDetailDTO importSlipDetailDTO : list){
            //     System.out.println(importSlipDetailDTO.toString());
            // }

            //Sua thong tin
            //ImportSlipDetailDTO detail = new ImportSlipDetailDTO(2222, 22111, 2112, 2222.0);
            // i.updateImportSlipDetail(detail);

            // Truy van
            // ImportSlipDetailDTO detail = i.getImportSlipDetailById(2222);
            // System.out.println(detail.toString());

            //Tạo đối tượng SupplierDAO
            SupplierDAO s = new SupplierDAO();
            // Them 
            // SupplierDTO supp = new SupplierDTO(112, "HChi", "123456788", "Quang Ngai", "Active");
            // s.insertSupplier(supp);

            //Lay danh sach
            // ArrayList<SupplierDTO> list = s.getAllSuppliers();
            // for (SupplierDTO supplierDTO : list){
            //     System.out.println(supplierDTO.toString());
            // }

            //Sua
            // SupplierDTO supp = new SupplierDTO(112, "HChine", "123456788", "Quang Ngai", "Activehh");
            // s.updateSupplier(supp);

            // Truy van
            // SupplierDTO supp = s.getSupplierById(112);
            // System.out.println(supp.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}