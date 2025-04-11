package com.bookstore.controller;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.bookstore.BUS.POSBUS;
import com.bookstore.gui.component.CategoryList;
import com.bookstore.gui.component.ProductCard;
import com.bookstore.gui.main.POSGUI;
import com.bookstore.model.Customer;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;

public class POSController {
    private POSGUI gui;
    private POSBUS bus;

    public POSController(POSGUI gui) throws Exception{
        this.gui = gui;
        this.bus = new POSBUS();
    }

    public void displayAllData() throws Exception{
        displayCategory();
    }

    public void displayCategory() throws Exception{
        gui.displayCategory(bus.loadingCategory());
        handleSearchProduct();
    }
    
    public void handleAddToCart(ActionEvent e){
        ProductCard source = (ProductCard) e.getSource();
        Product item = source.getProduct();
        int productId = item.getId();
        if (gui.getCart().containsKey(productId)) {
            OrderDetail detail = gui.getCart().get(productId);
            detail.setQuantity(detail.getQuantity() + 1);
        } else {
            OrderDetail newDetail = new OrderDetail(null, null, item, 1, item.getPrice());
            gui.getCart().put(productId, newDetail);
        }
        gui.displayCart();
        gui.onChangeCart();
    } 
 
    public void handleSearchProduct(){
        gui.getSearchProductField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                gui.displayProduct(bus.searchProduct(getValue()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                gui.displayProduct(bus.searchProduct(getValue()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                gui.displayProduct(bus.searchProduct(getValue()));
            }
            private String getValue(){
                return gui.getSearchProductField().getText();
            }
        }); 
    }

    public void handleCheckout(double moneyReceived) throws Exception{
        if(bus.checkout(bus.changeCartToOrder(gui.getCart()), gui.getEmployee(), gui.getSelectedCustomer(), moneyReceived)){   
            gui.getCart().clear();
        }
    }
  

    public void displayProductOnCategory(String value) throws SQLException{
        gui.displayProduct(bus.getAllProductFilterByCategory(), value);
    }




    
}
