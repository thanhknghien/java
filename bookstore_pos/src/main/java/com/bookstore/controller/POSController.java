package com.bookstore.controller;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.bookstore.BUS.POSBUS;
import com.bookstore.gui.component.CustomerDialog;
import com.bookstore.gui.component.ProductCard;
import com.bookstore.gui.main.POSGUI;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;
import com.bookstore.util.MessageUtil;

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
            MessageUtil.showSuccessMessage(gui, "Thanh toán thành công!");
            gui.getCart().clear();
            gui.displayCart();
        }else{
            MessageUtil.showErrorMessage(gui, "Thanh toán thất bại!");
        }
    }
  

    public void displayProductOnCategory(String value) throws SQLException{
        gui.displayProduct(bus.getCategoryAndProduct(), value);
    }

    public void handleSearchCustomer(){
        CustomerDialog dialog = new CustomerDialog(gui, bus.getCustomerList());
        gui.setSelectedCustomer(dialog.getSelectedCustomer());
        gui.displayCustomerSelected(gui.getSelectedCustomer());
    }

    public void handleAddCustomer() throws SQLException{
        CustomerDialog dialog = new CustomerDialog(bus.getCustomerList() ,gui);
        bus.addNewCustomer(dialog.getSelectedCustomer());
        gui.setSelectedCustomer(dialog.getSelectedCustomer());
        gui.displayCustomerSelected(gui.getSelectedCustomer());
    }
    
}
