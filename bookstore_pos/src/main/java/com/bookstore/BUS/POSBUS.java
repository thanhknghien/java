package com.bookstore.BUS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.bookstore.model.Customer;
import com.bookstore.model.Order;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;
import com.bookstore.model.User;

public class POSBUS {
    private ProductBUS productBUS;
    private CustomerBUS1 customerBUS;
    private ArrayList<Product> productList;
    private ArrayList<Customer> customerList;
    private Map<String, ArrayList<Product>> categoryAndProduct;

    public POSBUS() throws SQLException{
        productBUS = new ProductBUS();
        customerBUS = new CustomerBUS1();
        this.productList = productBUS.getAllProducts();
        this.categoryAndProduct = productBUS.getAllProductsByCategory();
        this.customerList = customerBUS.getALlCustomers();
    }

    // Loading Category
    public Map<String, ArrayList<Product>> loadingCategory() throws Exception{
        try {
            return productBUS.getAllProductsByCategory();
        } catch (SQLException e) {
            throw new Exception("Lỗi khi tải danh mục sản phẩm");
        }
    }

    public Map<String, ArrayList<Product>> getAllProductFilterByCategory() throws SQLException{
        return productBUS.getAllProductsByCategory();
    }

    public ArrayList<Product> searchProduct(String value){
        return productBUS.searchProducts(productList, value);
    }

    public boolean checkout(ArrayList<OrderDetail> details, User employee, Customer customer, double moneyReceived) throws Exception{
        OrderBUS orderBUS = new OrderBUS();
        Order order = orderBUS.createOrder(details, employee, customer);
        return orderBUS.printReceipt(order.getId(), "templates/order.html", "HoaDon"+String.valueOf(order.getId())+".pdf", moneyReceived);
    }

    public ArrayList<OrderDetail> changeCartToOrder(Map<Integer, OrderDetail> cart){
        return new ArrayList<>(cart.values());
    }

    public Map<String, ArrayList<Product>> getCategoryAndProduct() {
        return categoryAndProduct;
    }

    public ArrayList<Customer> seachCustomer(String value){
        return customerBUS.searchCustomer(customerList, value);
    }

    public ArrayList<Customer> getCustomerList() {
        return customerList;
    }

    public boolean addNewCustomer(Customer newCustomer) throws SQLException{
        boolean bo = customerBUS.addCustomer(newCustomer);
        this.customerList = customerBUS.getALlCustomers();
        return bo;
    }


    
}
