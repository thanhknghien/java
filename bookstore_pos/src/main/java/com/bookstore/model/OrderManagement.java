package com.bookstore.model;

public class OrderManagement {
    private int id;
    private boolean canView;

    public OrderManagement(int id, boolean canView) {
        this.id = id;
        this.canView = canView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }
}