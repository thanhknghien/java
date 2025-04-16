package com.bookstore.util;

import com.bookstore.model.User;
import com.bookstore.model.UserManagement;
import com.bookstore.dao.UserManagementDAO;

import java.sql.SQLException;

/**
 * Lớp quản lý phiên làm việc của người dùng
 * Sử dụng mẫu thiết kế Singleton để đảm bảo chỉ có một phiên làm việc trong toàn bộ ứng dụng
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private UserManagement userPermissions;
    private UserManagementDAO userManagementDAO;
    
    private SessionManager() {
        try {
            userManagementDAO = new UserManagementDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Lấy thể hiện duy nhất của SessionManager
     * @return Thể hiện của SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Thiết lập người dùng hiện tại và tải quyền của họ
     * @param user Người dùng đăng nhập
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadUserPermissions();
    }
    
    /**
     * Lấy thông tin người dùng hiện tại
     * @return Đối tượng User của người dùng đăng nhập
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Tải thông tin quyền của người dùng hiện tại
     */
    private void loadUserPermissions() {
        if (currentUser != null && userManagementDAO != null) {
            try {
                userPermissions = userManagementDAO.getById(currentUser.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Kiểm tra người dùng có quyền thêm không
     * @return true nếu có quyền, false nếu không
     */
    public boolean canAdd() {
        return userPermissions != null && userPermissions.isCanAdd();
    }
    
    /**
     * Kiểm tra người dùng có quyền chỉnh sửa không
     * @return true nếu có quyền, false nếu không
     */
    public boolean canEdit() {
        return userPermissions != null && userPermissions.isCanEdit();
    }
    
    /**
     * Kiểm tra người dùng có quyền xóa không
     * @return true nếu có quyền, false nếu không
     */
    public boolean canDelete() {
        return userPermissions != null && userPermissions.isCanDelete();
    }
    
    /**
     * Kiểm tra người dùng có quyền xem không
     * @return true nếu có quyền, false nếu không
     */
    public boolean canView() {
        return userPermissions != null && userPermissions.isCanView();
    }
    
    /**
     * Đăng xuất khỏi hệ thống
     */
    public void logout() {
        currentUser = null;
        userPermissions = null;
    }
}