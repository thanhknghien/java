package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;
import com.bookstore.model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
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
        this.product = product; // Gán product để sử dụng sau này
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
    
        // Trung tâm: tên + tác giả + giá
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setOpaque(false);
    
        lblName = new JLabel(product.getName(), SwingConstants.CENTER);
        lblAuthor = new JLabel("Tác giả: " + product.getAuthor(), SwingConstants.CENTER);
        lblPrice = new JLabel(String.format("%.0f₫", product.getPrice()), SwingConstants.CENTER);
        lblPrice.setForeground(ColorScheme.ERROR);
    
        ColorScheme.styleLabel(lblName, true);
        ColorScheme.styleLabel(lblAuthor, false);
        ColorScheme.styleLabel(lblPrice, true);
    
        infoPanel.add(lblName);
        infoPanel.add(lblAuthor);
        infoPanel.add(lblPrice);
        add(infoPanel, BorderLayout.CENTER);
    
        // Nút thêm
        btnAdd = new Button("Thêm");
        ColorScheme.styleButton(btnAdd, true);
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
        String finalPath;
    
        // Nếu null hoặc trống thì dùng ảnh mặc định
        if (path == null || path.trim().isEmpty()) {
            finalPath = "/product/default_img.png";  // Đường dẫn trong resources
        } else {
            File file = new File(path);
            if (file.exists()) {
                return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(160, 100, Image.SCALE_SMOOTH));
            } else {
                finalPath = "product/defauly_img.png";  // fallback nếu đường dẫn không tồn tại
            }
        }
    
        // Load từ resources
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(finalPath));
            Image scaled = icon.getImage().getScaledInstance(160, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            // Trường hợp file trong resources bị thiếu
            return new ImageIcon(new BufferedImage(160, 100, BufferedImage.TYPE_INT_RGB));
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