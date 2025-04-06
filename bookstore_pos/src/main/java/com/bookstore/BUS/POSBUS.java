package com.bookstore.BUS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.bookstore.model.Product;

public class POSBUS {
    private ProductBUS productBUS;
    private OrderBUS orderBUS;

    public POSBUS(){
        productBUS = new ProductBUS();
        orderBUS = new OrderBUS();
    }

    // Loading Category
    public Map<String, ArrayList<Product>> loadingCategory() throws Exception{
        try {
            return productBUS.getAllProductsByCategory();
        } catch (SQLException e) {
            throw new Exception("Lỗi khi tải danh mục sản phẩm");
        }
    }

    // Filter Product By Name
    public ArrayList<Product> geProductByName(String productName) throws SQLException{
        Map<String, String> criteria = new HashMap<>();
        criteria.put("name", productName);
        return productBUS.searchProducts(criteria);
    }

    // 
}
