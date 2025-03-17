/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.view.panel;

import com.bookstore.model.DAO.AccountDAO;
import com.bookstore.model.DTO.AccountDTO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class AccountManagementUI extends JFrame{
    private JTextField txtUsername, txtPassword, txtFullName, txtEmail, txtPhone, txtAddress, txtRoleId, txtEmployeeId;
    private JTable table;
    private DefaultTableModel tableModel;
    private AccountDAO accountDAO;

    public AccountManagementUI() {
        accountDAO = new AccountDAO();
        setTitle("Quản lý tài khoản");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(10, 2));
        
        panel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Password:"));
        txtPassword = new JTextField();
        panel.add(txtPassword);

        panel.add(new JLabel("Full Name:"));
        txtFullName = new JTextField();
        panel.add(txtFullName);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        panel.add(txtPhone);

        panel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        panel.add(txtAddress);

        panel.add(new JLabel("Role ID:"));
        txtRoleId = new JTextField();
        panel.add(txtRoleId);

        panel.add(new JLabel("Employee ID:"));
        txtEmployeeId = new JTextField();
        panel.add(txtEmployeeId);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAccount();
            }
        });
        panel.add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAccount();
            }
        });
        panel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });
        panel.add(btnDelete);

        JButton btnView = new JButton("View All");
        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllAccounts();
            }
        });
        panel.add(btnView);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Username", "Full Name", "Email", "Phone", "Address", "Role ID", "Employee ID"});
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addAccount() {
        AccountDTO account = new AccountDTO(0, txtUsername.getText(), txtPassword.getText(), txtFullName.getText(), txtEmail.getText(), txtPhone.getText(), txtAddress.getText(), Integer.parseInt(txtRoleId.getText()), Integer.parseInt(txtEmployeeId.getText()));
        try {
            boolean result = accountDAO.addAccount(account);
            if (result) {
                JOptionPane.showMessageDialog(this, "Account added successfully!");
                viewAllAccounts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add account.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAccount() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int accountId = (int) tableModel.getValueAt(selectedRow, 0);
            AccountDTO account = new AccountDTO(accountId, txtUsername.getText(), txtPassword.getText(), txtFullName.getText(), txtEmail.getText(), txtPhone.getText(), txtAddress.getText(), Integer.parseInt(txtRoleId.getText()), Integer.parseInt(txtEmployeeId.getText()));
            try {
                boolean result = accountDAO.updateAccount(account);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Account updated successfully!");
                    viewAllAccounts();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update account.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an account to update.");
        }
    }

    private void deleteAccount() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int accountId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                boolean result = accountDAO.deleteAccount(accountId);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Account deleted successfully!");
                    viewAllAccounts();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete account.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an account to delete.");
        }
    }

    private void viewAllAccounts() {
        try {
            ArrayList<AccountDTO> accounts = accountDAO.getAllAccounts();
            tableModel.setRowCount(0);
            for (AccountDTO account : accounts) {
                tableModel.addRow(new Object[]{account.getAccountId(), account.getUsername(), account.getFullName(), account.getEmail(), account.getPhone(), account.getAddress(), account.getRoleId(), account.getEmployeeId()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AccountManagementUI().setVisible(true);
            }
        });
    }
}
