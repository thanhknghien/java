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

    // loading Product By Category
    public ArrayList<Product> loadingProducts(Map<String, ArrayList<Product>> filterProducts, String categoryName){
        ArrayList<Product> products = filterProducts.get(categoryName);
        return products;
    }

    // Filter Product By Name
    public ArrayList<Product> getProductByName(String productName) throws SQLException{
        Map<String, String> criteria = new HashMap<>();
        criteria.put("name", productName);
        return productBUS.searchProducts(criteria);
    }

    


    
}
