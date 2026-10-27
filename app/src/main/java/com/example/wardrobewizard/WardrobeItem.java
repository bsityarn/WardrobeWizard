package com.example.wardrobewizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Simple UI-only model with hardcoded dummy data (no database yet). */
public class WardrobeItem {
    private final String name;
    private final String brand;
    private final String category;
    private final String description;

    public WardrobeItem(String name, String brand, String category, String description) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.description = description;
    }

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }

    public static List<WardrobeItem> dummyItems() {
        return new ArrayList<>(Arrays.asList(
                new WardrobeItem("White Tee", "Uniqlo", "Tops", "Plain white cotton t-shirt"),
                new WardrobeItem("Striped Polo", "Lacoste", "Tops", "Navy striped polo shirt"),
                new WardrobeItem("Denim Jeans", "Levi's", "Bottoms", "Slim fit blue jeans"),
                new WardrobeItem("Chinos", "Dockers", "Bottoms", "Beige chino trousers"),
                new WardrobeItem("Bomber Jacket", "Zara", "Jackets", "Olive bomber jacket"),
                new WardrobeItem("Sneakers", "Nike", "Shoes", "White low-top sneakers"),
                new WardrobeItem("Loafers", "Clarks", "Shoes", "Brown leather loafers"),
                new WardrobeItem("Watch", "Casio", "Accessories", "Silver analog watch")
        ));
    }

    public static List<WardrobeItem> byCategory(String category) {
        List<WardrobeItem> result = new ArrayList<>();
        for (WardrobeItem item : dummyItems()) {
            if ("All".equals(category) || item.getCategory().equals(category)) result.add(item);
        }
        return result;
    }
}
