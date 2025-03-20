/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.view.panel;

//import com.bookstore.controller.AccountManagementController;
import com.bookstore.model.DAO.AccountDAO;
import com.bookstore.model.DTO.AccountDTO;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author HP
 */
public class AccountManagementPanel extends JFrame{

    private JTextField searchField;
    private JButton searchButton = new JButton(new ImageIcon( "D:\\QLBH\\java\\bookstore\\src\\main\\java\\com\\bookstore\\resources\\icon\\search-icon-16x16.png"));
    private JTable accountTable;
    private DefaultTableModel tableModel; //

    public AccountManagementPanel() {
        setTitle("Quản Lý Tài Khoản");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); 

        //Tao controller
       // AccountManagementController accountManagementController = new AccountManagementController(this);
        
        JMenuBar jMenuBar = new JMenuBar();
        JButton addButton = new JButton(new ImageIcon("D:\\QLBH\\java\\bookstore\\src\\main\\java\\com\\bookstore\\resources\\icon\\Button-Add-icon-16x16.png"));
        JMenu filterButton = new JMenu("Filter ");        
        
        //Tao "search"
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField("Tìm kiếm",20);
      //  searchField.addActionListener(accountManagementController);
       // searchButton.addActionListener(accountManagementController);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        //searchPanel.setVisible(false);
        
        // Tao Button Filter
        JMenuItem customerFilter = new JMenuItem("customer");
       // customerFilter.addActionListener(accountManagementController);
        JMenuItem employeeFilter = new JMenuItem("employee");
       // employeeFilter.addActionListener(accountManagementController);
        filterButton.add(customerFilter);
        filterButton.addSeparator();
        filterButton.add(employeeFilter);
        
        jMenuBar.add(addButton);
        jMenuBar.add(filterButton);
        //jMenuBar.add(Box.createHorizontalGlue());
        jMenuBar.add(searchPanel);
        
        
        add(jMenuBar, BorderLayout.NORTH);
        // SOUTH: Bảng hiển thị thông tin tài khoản
        String[] columnNames = {"STT", "Tên Người Dùng", "Họ và Tên", "Mật Khẩu", "Email",
                "Số Điện Thoại", "Địa Chỉ", "RoleID", "Hành Động"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Cột cuối cùng (Hành động) mới được chỉnh sửa
                return column == 8;
            }
        };
        accountTable = new JTable(tableModel);
        accountTable.setRowHeight(25);
        // Thêm renderer và editor cho cột Hành động
        accountTable.getColumn("Hành Động").setCellRenderer(new ActionRenderer());
        accountTable.getColumn("Hành Động").setCellEditor(new ActionEditor());

        JScrollPane tableScrollPane = new JScrollPane(accountTable);
        add(tableScrollPane, BorderLayout.SOUTH);

        // Tải dữ liệu mẫu
        loadTableData();
    }

    // Dummy data
    private void loadTableData() {
        Object[][] data = {
                {1, "user1", "Nguyen Van A", "password1", "a@gmail.com", "0123456789", "HCM", 1, null},
                {2, "user2", "Tran Thi B", "password2", "b@gmail.com", "0987654321", "HN", 2, null}
        };
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    // Renderer hiển thị biểu tượng
    private class ActionRenderer extends JPanel implements TableCellRenderer {
        private final JButton editButton;
        private final JButton deleteButton;
        
        public ActionRenderer() {
            
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton(new ImageIcon("D:\\QLBH\\java\\bookstore\\src\\main\\java\\com\\bookstore\\resources\\icon\\edit-icon-8x8.png")); // Thay bằng icon chỉnh sửa
            deleteButton = new JButton(new ImageIcon("D:\\QLBH\\java\\bookstore\\src\\main\\java\\com\\bookstore\\resources\\icon\\Trash-can-icon-8x8.png")); // Thay bằng icon xóaURL urlIcon = AccountManagementPanel.class.getResource("edit-icon.png");
        
            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    //xử lý "Tìm kiếm"
    
    
    // Editor xử lý sự kiện khi nhấn nút
    private class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private final JButton editButton;
        private final JButton deleteButton;

        public ActionEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton(new ImageIcon("D:\\QLBH\\java\\bookstore\\src\\main\\java\\com\\bookstore\\resources\\icon\\edit-icon-8x8.png")); // Thay bằng icon chỉnh sửa
            deleteButton = new JButton(new ImageIcon("D:\\QLBH\\java\\bookstore\\src\\main\\java\\com\\bookstore\\resources\\icon\\Trash-can-icon-8x8.png")); // Thay bằng icon xóaURL urlIcon = AccountManagementPanel.class.getResource("edit-icon.png");


            panel.add(editButton);
            panel.add(deleteButton);

            // Xử lý nút sửa
            //editButton.addActionListener(e -> fireEditingStopped()); Nên được gọi bên trong openEditPage
            editButton.addActionListener(e -> openEditPage());

            // Xử lý nút xóa
            //deleteButton.addActionListener(e -> fireEditingStopped());
            deleteButton.addActionListener(e -> confirmDelete());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        // Chuyển đến trang chỉnh sửa
        private void openEditPage() {
            JOptionPane.showMessageDialog(AccountManagementPanel.this, "Chuyển sang trang chỉnh sửa!");
            
            // Code chuyển sang JFrame/JPanel chỉnh sửa
        }

        // Xác nhận xóa
        private void confirmDelete() {
            int confirm = JOptionPane.showConfirmDialog(AccountManagementPanel.this, "Bạn có chắc muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(AccountManagementPanel.this, "Tài khoản đã bị xóa.");
                // Code xóa tài khoản khỏi cơ sở dữ liệu
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AccountManagementPanel frame = new AccountManagementPanel();
            frame.setVisible(true);
        });
    }


    
    
  
}
