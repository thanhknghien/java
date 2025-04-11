package com.bookstore.model;

public class OrderDetail {
    private Integer id;
    private Integer orderId;
    private Product product;
    private int quantity;
    private double price;

    // Constructor
    public OrderDetail() {}

    public OrderDetail(Integer id, Integer orderId, Product product, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName(){
        return product.getName();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public double getSubtotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return "OrderDetail [id=" + id + ", orderId=" + orderId + ", product=" + product + ", quantity=" + quantity
                + ", price=" + price + "]";
    }
}