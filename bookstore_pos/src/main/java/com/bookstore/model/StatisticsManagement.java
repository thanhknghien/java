package com.bookstore.model;

public class StatisticsManagement {
    private int id;
    private boolean canView;

    public StatisticsManagement(int id, boolean canView) {
        this.id = id;
        this.canView = canView;
    }

    public int getId() {
        return id;
    }

    public boolean isCanView() {
        return canView;
    }
}