package com.bookstore.model;

public class CategoryManagement {
    private int id;
    private boolean canAdd;
    private boolean canEdit;
    private boolean canDelete;
    private boolean canView;

    // Constructor không tham số
    public CategoryManagement() {
    }

    public CategoryManagement(int id, boolean canAdd, boolean canEdit, boolean canDelete, boolean canView) {
        this.id = id;
        this.canAdd = canAdd;
        this.canEdit = canEdit;
        this.canDelete = canDelete;
        this.canView = canView;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public boolean isCanAdd() { return canAdd; }
    public void setCanAdd(boolean canAdd) { this.canAdd = canAdd; }
    public boolean isCanEdit() { return canEdit; }
    public void setCanEdit(boolean canEdit) { this.canEdit = canEdit; }
    public boolean isCanDelete() { return canDelete; }
    public void setCanDelete(boolean canDelete) { this.canDelete = canDelete; }
    public boolean isCanView() { return canView; }
    public void setCanView(boolean canView) { this.canView = canView; }
}