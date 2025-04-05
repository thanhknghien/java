package com.bookstore.model;

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String category;
    private String author;
    private String image;
    public Product() {
    }
    public Product(int id, String name, double price, int stock, String category, String author, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.author = author;
        this.image = image;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    } 
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}