package com.bookstore.BUS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.bookstore.dao.ProductDAO;
import com.bookstore.model.Product;

public class ProductBUS {
    private ProductDAO productDAO;

    public ProductBUS(){
        productDAO = new ProductDAO();
    }

    public ArrayList<Product> getAllProducts() throws SQLException{
        return productDAO.getAllProducts();
    }

    public Map<String, ArrayList<Product>> getAllProductsByCategory() throws SQLException {
        return productDAO.getAllProductsByCategory();
    }

    public ArrayList<Product> searchProducts(Map<String, String> criteria) throws SQLException{
        return productDAO.searchProducts(criteria);
    }

    // Filter Products By Range of Price
    public ArrayList<Product> getProductByRanggeOfPrice(String min, String max) throws SQLException{
        Map<String, String> criteria = new HashMap<>();
        criteria.put("price_min", min);
        criteria.put("price_max", max);
        return productDAO.searchProducts(criteria);
    }
}
