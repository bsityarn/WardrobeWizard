package com.example.wardrobewizard;

import java.util.List;

public class Category {
    private String name;
    private List<String> items;

    public Category(String name, List<String> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public List<String> getItems() {
        return items;
    }
}