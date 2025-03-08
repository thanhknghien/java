package com.bookstore.model.DTO;

import java.util.Date;
// Lớp hiệu quả khuyến mãi cấm đụng =)))
public class PromotionEffectivenessDTO {
    private String promotionName;
    private Date startDate;
    private Date endDate;
    private int totalOrdersUsed;
    private double totalDiscountAmount;

    public PromotionEffectivenessDTO(String promotionName, Date startDate, Date endDate, int totalOrdersUsed, double totalDiscountAmount) {
        this.promotionName = promotionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalOrdersUsed = totalOrdersUsed;
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public String getPromotionName() { 
        return promotionName; 
    }
    public Date getStartDate() { 
        return startDate; 
    }
    public Date getEndDate() { 
        return endDate; 
    }
    public int getTotalOrdersUsed() { 
        return totalOrdersUsed; 
    }
    public double getTotalDiscountAmount() {
        return totalDiscountAmount; 
    }
}
