package com.bookstore.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.TextField;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.component.PanelCover;
import com.bookstore.gui.util.FrameUtils;

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
    private Button clearBtn;

    private Button printPDFBtn;
    private Button exportExcelBtn;
    private Button importExcelbtn;

    // Main Panel
    private PanelCover mainPanel;
    private CustomTable ordersTable;
    private DefaultTableModel ordersTableModel;
    
    public OrderManagementPanel(){

    }

    // Init Components
    private void initializeUI(){
        // North Panel
        northPanel = new JPanel(new GridBagLayout());
        





    }







}
