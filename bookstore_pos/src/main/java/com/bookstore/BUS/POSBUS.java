package com.bookstore.BUS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.bookstore.model.Customer;
import com.bookstore.model.Product;

public class POSBUS {
    private ProductBUS productBUS;
    private OrderBUS orderBUS;
    private CustomerBUS customerBUS;
    private ArrayList<Product> products;
    private ArrayList<Customer> customers;

    public POSBUS(){
        productBUS = new ProductBUS();
        orderBUS = new OrderBUS();
        customerBUS = new CustomerBUS();
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

    // Filter Product By Name or Author
    public ArrayList<Product> getProductByName(ArrayList<Product> products, String value ) throws SQLException{
        return productBUS.searhProducts(products, value);
    }

    // get all customers
    public ArrayList<Customer> getAllCustomers() throws SQLException{
        return customerBUS.getALlCustomers();
    }

    // Search Customer
    public ArrayList<Customer> searchCustomers(ArrayList<Customer> list, String value) throws SQLException{
        return customerBUS.searchCustomer(list, value);
    }

    
}
