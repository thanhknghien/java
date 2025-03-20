package com.bookstore.view.dialog;

import com.bookstore.utils.Status;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class EditAccountDialog extends JDialog {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JComboBox<String> comboRole;
    private JPanel statusPanel;
    private JComboBox<Status> comboStatus;
    private JButton btnSave;
    private JButton btnCancel;
    
    // Constructor truyền data của tài khoản (theo mảng Object)
    public EditAccountDialog(JFrame parent, Object[] accountData) {
        super(parent, "Sửa Tài Khoản", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tên người dùng
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Tên người dùng:"), gbc);
        txtUsername = new JTextField(accountData[1].toString());
        gbc.gridx = 1;
        panelForm.add(txtUsername, gbc);
        
        // Mật khẩu
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Mật khẩu:"), gbc);
        txtPassword = new JPasswordField(accountData[3].toString());
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);
        
        // Họ và tên
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Họ và tên:"), gbc);
        txtFullName = new JTextField(accountData[2].toString());
        gbc.gridx = 1;
        panelForm.add(txtFullName, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(accountData[4].toString());
        gbc.gridx = 1;
        panelForm.add(txtEmail, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(new JLabel("Phone:"), gbc);
        txtPhone = new JTextField(accountData[5].toString());
        gbc.gridx = 1;
        panelForm.add(txtPhone, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 5;
        panelForm.add(new JLabel("Address:"), gbc);
        txtAddress = new JTextField(accountData[6].toString());
        gbc.gridx = 1;
        panelForm.add(txtAddress, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 6;
        panelForm.add(new JLabel("Role:"), gbc);
        String[] roles = {"Khách hàng (1)", "Nhân viên (2)"};
        comboRole = new JComboBox<>(roles);
        int roleIndex = (accountData[7] != null && !accountData[7].toString().isEmpty()) ? 1 : 0;
        comboRole.setSelectedIndex(roleIndex);
        gbc.gridx = 1;
        panelForm.add(comboRole, gbc);
        
        // Status (chỉ nếu nhân viên)
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(Status.values());
        if (roleIndex == 1) {
            // Giả sử accountData[7] chứa trạng thái dưới dạng String (ví dụ "ACTIVE")
            String statusStr = accountData[7].toString();
            int selIndex = 0;
            for (int i = 0; i < Status.values().length; i++) {
                if (Status.values()[i].toString().equalsIgnoreCase(statusStr)) {
                    selIndex = i;
                    break;
                }
            }
            comboStatus.setSelectedIndex(selIndex);
            statusPanel.setVisible(true);
        } else {
            statusPanel.setVisible(false);
        }
        statusPanel.add(comboStatus);
        panelForm.add(statusPanel, gbc);
        
        comboRole.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                statusPanel.setVisible(comboRole.getSelectedIndex() == 1);
                panelForm.revalidate();
                panelForm.repaint();
            }
        });
        
        add(panelForm, BorderLayout.CENTER);
        
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        add(panelButtons, BorderLayout.SOUTH);
        
        btnCancel.addActionListener(e -> dispose());
    }
    
    // Các getter cho controller
    public String getUsername() { return txtUsername.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public String getFullName() { return txtFullName.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getPhone() { return txtPhone.getText().trim(); }
    public String getAddress() { return txtAddress.getText().trim(); }
    public int getRoleId() { return comboRole.getSelectedIndex() == 0 ? 1 : 2; }
    public Status getStatus() { return (Status) comboStatus.getSelectedItem(); }
    public JButton getSaveButton() { return btnSave; }
}
