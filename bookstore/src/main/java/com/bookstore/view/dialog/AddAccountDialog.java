/*package view.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddAccountDialog extends JDialog {
    // Các thành phần giao diện như đã định nghĩa trước...
    private JTextField txtUsername;
    private JTextField txtFullName;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JComboBox<String> comboRole;
    
    private JPanel panelStatus;
    private JTextField txtStatus;
    
    private JButton btnSave;
    private JButton btnCancel;
    
    public AddAccountDialog(JFrame parent) {
        super(parent, "Thêm Tài Khoản", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // --- CENTER: Form nhập thông tin ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0: Tên người dùng
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Tên người dùng:"), gbc);
        txtUsername = new JTextField();
        gbc.gridx = 1;
        panelForm.add(txtUsername, gbc);
        
        // Row 1: Họ và tên
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Họ và tên:"), gbc);
        txtFullName = new JTextField();
        gbc.gridx = 1;
        panelForm.add(txtFullName, gbc);
        
        // Row 2: Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Mật khẩu:"), gbc);
        txtPassword = new JPasswordField();
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);
        
        // Row 3: Xác nhận mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        txtConfirmPassword = new JPasswordField();
        gbc.gridx = 1;
        panelForm.add(txtConfirmPassword, gbc);
        
        // Row 4: Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelForm.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField();
        gbc.gridx = 1;
        panelForm.add(txtEmail, gbc);
        
        // Row 5: Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelForm.add(new JLabel("Số điện thoại:"), gbc);
        txtPhone = new JTextField();
        gbc.gridx = 1;
        panelForm.add(txtPhone, gbc);
        
        // Row 6: Role (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 6;
        panelForm.add(new JLabel("Role:"), gbc);
        String[] roles = {"Khách hàng (1)", "Nhân viên (2)"};
        comboRole = new JComboBox<>(roles);
        gbc.gridx = 1;
        panelForm.add(comboRole, gbc);
        
        // Row 7: Panel chứa Status (chỉ hiển thị nếu chọn "Nhân viên")
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panelStatus = new JPanel(new BorderLayout());
        panelStatus.add(new JLabel("Status:"), BorderLayout.WEST);
        txtStatus = new JTextField();
        panelStatus.add(txtStatus, BorderLayout.CENTER);
        panelForm.add(panelStatus, gbc);
        panelStatus.setVisible(false);
        
        comboRole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isEmployee = comboRole.getSelectedIndex() == 1;
                panelStatus.setVisible(isEmployee);
                panelForm.revalidate();
                panelForm.repaint();
            }
        });
        
        add(panelForm, BorderLayout.CENTER);
        
        // --- SOUTH: Nút Lưu và Hủy ---
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        add(panelButtons, BorderLayout.SOUTH);
        
        btnCancel.addActionListener(e -> dispose());
    }
    
    // Các getter để lấy dữ liệu nhập vào từ dialog (nếu cần)
    public JButton getSaveButton() {
        return btnSave;
    }
    
    public static void main(String[] args) {
        // Sử dụng SwingUtilities.invokeLater để đảm bảo giao diện chạy trên EDT
        SwingUtilities.invokeLater(() -> {
            // Tạo một JFrame dummy làm parent cho dialog
            JFrame dummy = new JFrame();
            dummy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dummy.setSize(300, 200);
            dummy.setLocationRelativeTo(null);
            dummy.setVisible(true);
            
            // Khởi tạo và hiển thị dialog
            AddAccountDialog dialog = new AddAccountDialog(dummy);
            dialog.setVisible(true);
        });
    }
*/