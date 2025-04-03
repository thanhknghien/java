package com.bookstore.gui.panel;

import java.awt.TextField;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;

public class OrderManagementPanel extends JPanel {
    // North Panel
    private JPanel northPanel;
    
    // Input Field (North Panel)
    private TextField orderIdField;
    private TextField customerIdField;
    private TextField employeeIdField;
    private TextField createdDateField;
    private TextField totalField;
    
    // Function Button (North Panel)
    private Button searchBtn;
    private Button addBtn;
    private Button deleteBtn;
    private Button updateBtn;
    private Button viewDetailBtn;
    private Button refreshBtn;
    private Button printPDFBtn;
    private Button exportExcelBtn;
    private Button importExcelbtn;

    // Main Panel
    private CustomTable ordersTable;
    private DefaultTableModel ordersTableModel;
    
    public OrderManagementPanel(){

    }

    // Init Components
    private void initializeUI(){
        
    }







}
