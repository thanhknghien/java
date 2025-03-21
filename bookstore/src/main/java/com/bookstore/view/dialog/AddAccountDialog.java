package com.bookstore.view.dialog;

import com.bookstore.utils.Status;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddAccountDialog extends JDialog {
    private JTextField txtUsername;
    private JTextField txtFullName;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JComboBox<String> comboRole;
    private JPanel statusPanel;
    private JComboBox<Status> comboStatus;
    private JButton btnSave;
    private JButton btnCancel;

    public AddAccountDialog(JFrame parent) {
        super(parent, "Thêm Tài Khoản", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // CENTER: Form nhập
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tên người dùng
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Tên người dùng:"), gbc);
        txtUsername = new JTextField();
        gbc.gridx = 1; 
        panelForm.add(txtUsername, gbc);
        
        // Họ và tên
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Họ và tên:"), gbc);
        txtFullName = new JTextField();
        gbc.gridx = 1;
        panelForm.add(txtFullName, gbc);
        
        // Mật khẩu
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Mật khẩu:"), gbc);
        txtPassword = new JPasswordField();
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);
        
        // Xác nhận mật khẩu
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        txtConfirmPassword = new JPasswordField();
        gbc.gridx = 1;
        panelForm.add(txtConfirmPassword, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField();
        gbc.gridx = 1;
        panelForm.add(txtEmail, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 5;
        panelForm.add(new JLabel("Phone:"), gbc);
        txtPhone = new JTextField();
        gbc.gridx = 1;
        panelForm.add(txtPhone, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 6;
        panelForm.add(new JLabel("Address:"), gbc);
        txtAddress = new JTextField();
        gbc.gridx = 1;
        panelForm.add(txtAddress, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 7;
        panelForm.add(new JLabel("Role:"), gbc);
        String[] roles = {"Khách hàng (1)", "Nhân viên (2)"};
        comboRole = new JComboBox<>(roles);
        gbc.gridx = 1;
        panelForm.add(comboRole, gbc);
        
        // Status (hiển thị nếu nhân viên)
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(Status.values());
        statusPanel.add(comboStatus);
        statusPanel.setVisible(false);
        panelForm.add(statusPanel, gbc);
        
        comboRole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusPanel.setVisible(comboRole.getSelectedIndex() == 1);
                panelForm.revalidate();
                panelForm.repaint();
            }
        });
        
        add(panelForm, BorderLayout.CENTER);
        
        // SOUTH: Nút Lưu và Hủy
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        add(panelButtons, BorderLayout.SOUTH);
        
        btnCancel.addActionListener(e -> dispose());
    }

    // Các getter dùng cho AccountController
    public String getUsername() { return txtUsername.getText().trim(); }
    public String getFullName() { return txtFullName.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public String getConfirmPassword() { return new String(txtConfirmPassword.getPassword()); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getPhone() { return txtPhone.getText().trim(); }
    public String getAddress() { return txtAddress.getText().trim(); }
    public int getRoleId() { return comboRole.getSelectedIndex() == 0 ? 1 : 2; }
    public Status getStatus() { return (Status) comboStatus.getSelectedItem(); }
    public JButton getSaveButton() { return btnSave; }
}