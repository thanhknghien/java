/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.view.panel;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author HP
 */
public class ManageAccount extends JFrame{

    public ManageAccount() {
        this.setSize(600,400);
        this.setLocation(500,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void showIt (){
        this.setVisible(true);
        
       
    }
    public void showIt(String title){
        this.setTitle(title);
        this.setVisible(true);
    }
    public static void main(String[] args) {
        ManageAccount mw1= new ManageAccount();
        mw1.showIt();
        ManageAccount mw2 = new ManageAccount();
        mw2.showIt("Window2");
    }
}
