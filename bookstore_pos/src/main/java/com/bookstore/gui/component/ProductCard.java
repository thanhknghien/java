package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;
import com.bookstore.model.Product;
import com.bookstore.util.NumberUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ProductCard extends JPanel {
    private JLabel lblName;
    private JLabel lblPrice;
    private JLabel lblAuthor;
    private JLabel lblImage;
    private Button btnAdd;
    private Product product;
    private ArrayList<ActionListener> addToCartListeners = new ArrayList<>();

    public ProductCard(Product product) {
        this.product = product; 
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(180, 220));
        setBackground(ColorScheme.SURFACE);
        setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
    
        // Ảnh sản phẩm
        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icon = loadImage(product.getImagePath());
        lblImage.setIcon(icon);
        add(lblImage, BorderLayout.NORTH);
    
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setOpaque(false);
    
        lblName = new JLabel(product.getName(), SwingConstants.CENTER);
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblAuthor = new JLabel("Tác giả: " + product.getAuthor(), SwingConstants.CENTER);
        lblAuthor.setFont(new Font("Arial", Font.ITALIC, 12));
        lblPrice = new JLabel( NumberUtil.formatNumber(product.getPrice()) + " Đ", SwingConstants.CENTER);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 16));
        lblPrice.setForeground(ColorScheme.ERROR);
    
        infoPanel.add(lblName);
        infoPanel.add(lblAuthor);
        infoPanel.add(lblPrice);
        add(infoPanel, BorderLayout.CENTER);
    
        // Nút thêm
        btnAdd = new Button("Thêm");
        add(btnAdd, BorderLayout.SOUTH);
    
        // Gắn sự kiện cho nút "Thêm"
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireAddToCartEvent(); 
            }
        });
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

        private ImageIcon loadImage(String path) {
        final int WIDTH = 160;
        final int HEIGHT = 100;
        final String DEFAULT_IMAGE_PATH = "product/default_img.png";
    
        try {
            String finalPath = path;

            if (path == null || path.trim().isEmpty()) {
                finalPath = DEFAULT_IMAGE_PATH;
            }

            java.net.URL imageUrl = getClass().getClassLoader().getResource(finalPath);
            if (imageUrl == null) {
                finalPath = DEFAULT_IMAGE_PATH;
                imageUrl = getClass().getClassLoader().getResource(finalPath);
            }

            if (imageUrl == null) {
                BufferedImage emptyImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                return new ImageIcon(emptyImage);
            }
    
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaledImage = icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
    
        } catch (Exception e) {
            System.err.println("Error loading image: " + path + " - " + e.getMessage());
            BufferedImage emptyImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = emptyImage.createGraphics();
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            g2d.dispose();
            return new ImageIcon(emptyImage);
        }
    }
        
    
        
    private void fireAddToCartEvent() {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "addToCart");
        for (ActionListener listener : addToCartListeners) {
            listener.actionPerformed(evt);
        }
    }
    public void addAddToCartListener(ActionListener listener) {
        addToCartListeners.add(listener);
    }

    public Product getProduct() {
        return product;
    }
}