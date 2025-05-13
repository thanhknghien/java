package com.bookstore.BUS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.bookstore.dao.ProductDAO;
import com.bookstore.dao.CategoryDAO;
import com.bookstore.dao.UserDAO;
import com.bookstore.model.Product;
import com.bookstore.model.User;
import com.bookstore.util.NomalizeVietnamese;
import com.bookstore.util.SessionManager;
import java.util.List;

public class ProductBUS {
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private UserDAO userDAO;

    public ProductBUS(){
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        userDAO = new UserDAO();
    }

    public ArrayList<Product> getAllProducts() throws SQLException{
        return productDAO.getAllProducts();
    }

    public Map<String, ArrayList<Product>> getAllProductsByCategory() throws SQLException {
        return productDAO.getAllProductsByCategory();
    }

    // tim kiem nâng cao
    public ArrayList<Product> searchProducts(Map<String, String> criteria) throws SQLException{
        return productDAO.searchProducts(criteria);
    }

    // Filter with Name or Author's name
    public ArrayList<Product> searchProducts(ArrayList<Product> list, String value){
        ArrayList<Product> resuls = new ArrayList<>();
        value = NomalizeVietnamese.normalizeVietnamese(value).toLowerCase();
        for(Product l : list){

            String normalizedName = NomalizeVietnamese.normalizeVietnamese(l.getName()).toLowerCase();
            String normalizedAuthor = NomalizeVietnamese.normalizeVietnamese(l.getAuthor()).toLowerCase();
        
            if (normalizedName.contains(value) || normalizedAuthor.contains(value)) {
                resuls.add(l);
            }
        }
        return resuls;
    }

    // Filter Products By Range of Price
    public ArrayList<Product> getProductByRanggeOfPrice(String min, String max) throws SQLException{
        Map<String, String> criteria = new HashMap<>();
        criteria.put("price_min", min);
        criteria.put("price_max", max);
        return productDAO.searchProducts(criteria);
    }
    
    public int addProduct(Product product) throws SQLException{
        if(product == null){
            throw new IllegalArgumentException("Sản phẩm không được null");
        }
        if(product.getName() == null || product.getName().trim().isEmpty()){
            throw new IllegalArgumentException("Tên sản phẩm không được để trống!");
        }
        if(product.getAuthor() == null || product.getAuthor().trim().isEmpty()){
            throw new IllegalArgumentException("Tên tác giả không được để trống!");
        }
        if(product.getPrice() < 0){
            throw new IllegalArgumentException("Giá sản phẩm không được âm!");
        }
        if(product.getCategoryId() <= 0){
            throw new IllegalArgumentException("ID danh mục không hợp lệ!");
        }
        if (!categoryDAO.exists(product.getCategoryId())) {
            throw new IllegalArgumentException("ID danh mục không tồn tại trong hệ thống.");
        }
        return productDAO.addProduct(product);
    }
    
    public boolean deleteProduct(int id) throws SQLException{
        productDAO.deleteProduct(id);
        return true;
    }
    
    public boolean updateProduct(Product product) throws SQLException{
       productDAO.updateProduct(product);
       return true;
    }
    
    public Product searchById(int id) throws SQLException{
        if(id <= 0){
            throw new IllegalArgumentException("ID sản phẩm không hợp lệ!");
        }
        return productDAO.searchById(id);
    }
    
    public List<Product> searchByName(String name)throws SQLException{
        if(name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Tên sản phẩm không được để trống!");
        }
        return productDAO.searchByName(name);
    }
    
    public List<Product> searchByAuthor(String author) throws SQLException{
        if(author == null || author.trim().isEmpty()){
            throw new IllegalArgumentException("Tên tác giả không được để trống!");
        }
        return productDAO.searchByAuthor(author);
    }
    
    public List<Product> searchByCategoryId(int categoryId) throws SQLException{
        if(categoryId <= 0){
            throw new IllegalArgumentException("ID danh mục không hợp lệ!");
        }
        return productDAO.searchByCategoryId(categoryId);
    }
    public ArrayList<String> getAllPermissions(int userId) throws SQLException {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        SessionManager sessionManager = SessionManager.getInstance();
        User currentUser = sessionManager.getCurrentUser();
        sessionManager.setCurrentUser(user);
        
        try {
            ArrayList<ArrayList<String>> allPermissions = sessionManager.getAllPermissions();
            return allPermissions.get(1); // user_management permissions
        } finally {
            sessionManager.setCurrentUser(currentUser);
        }
    }
}
