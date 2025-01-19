package com.example.demo.dtos;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> items;
    private long totalItems;

    public PaginatedResponse(List<T> items, long totalItems) {
        this.items = items;
        this.totalItems = totalItems;
    }

    // Getters and setters
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
