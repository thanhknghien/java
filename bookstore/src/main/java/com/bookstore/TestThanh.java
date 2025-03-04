package com.bookstore;

import java.sql.SQLException;
import java.util.ArrayList;


import com.bookstore.model.DAO.AccountDAO;
import com.bookstore.model.DAO.RoleDAO;
import com.bookstore.model.DTO.AccountDTO;
import com.bookstore.model.DTO.RoleDTO;


public class TestThanh {
    public static void main(String[] args) throws SQLException {
        RoleDAO dao = new RoleDAO();
        RoleDTO dto = new RoleDTO();
        ArrayList<RoleDTO> list = new ArrayList<RoleDTO>();
        list = dao.getAllRoles();
        for(RoleDTO l : list){
            System.out.println(l.toString());
        }
        
    }
}
