package com.example.wardrobewizard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private ChipGroup chipGroupCategories;
    private RecyclerView rvSpecificItems;
    private ItemAdapter itemAdapter;
    private List<Category> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        rvSpecificItems = findViewById(R.id.rvSpecificItems);

        // Populate category & item data
        initData();

        // Configure 2-column Grid Layout Manager
        rvSpecificItems.setLayoutManager(new GridLayoutManager(this, 2));
        itemAdapter = new ItemAdapter(new ArrayList<>(), item -> {
            Intent intent = new Intent(AddItemActivity.this, CameraActivity.class);
            intent.putExtra("selectedItem", item);
            startActivity(intent);
        });
        rvSpecificItems.setAdapter(itemAdapter);

        // Populate Chips & render initial items
        setupCategoryChips();
    }

    private void initData() {
        categoriesList = new ArrayList<>();

        categoriesList.add(new Category("Men's Tops", Arrays.asList(
                "T-Shirts", "Polo Shirts", "Button-Down Shirts", "Henleys", "Sleeveless"
        )));

        categoriesList.add(new Category("Men's Bottoms", Arrays.asList(
                "Denim Jeans", "Chinos & Trousers", "Dress Pants", "Casual Shorts", "Athletic Shorts", "Sweatpants"
        )));

        categoriesList.add(new Category("Outerwear & Suits", Arrays.asList(
                "Suit Blazer", "Vests", "Jackets", "Overcoats & Trench Coats", "Puffer Jackets", "Sweaters & Knitwear", "Cardigans", "Hoodies"
        )));

        categoriesList.add(new Category("Footwear", Arrays.asList(
                "Sneakers", "Loafers & Slip-ons", "Dress Shoes", "Boots", "Sandals & Slides", "Athletic Shoes", "Heels"
        )));

        categoriesList.add(new Category("Accessories", Arrays.asList(
                "Bags", "Headwear", "Eyewear", "Belts", "Scarves & Gloves", "Ties", "Bowties", "Necklaces", "Bracelets", "Rings", "Earings", "Watches"
        )));

        categoriesList.add(new Category("Women's Tops", Arrays.asList(
                "Blouses", "T-Shirts", "Crop Tops", "Camisoles & Tank Tops", "Bodysuits"
        )));

        categoriesList.add(new Category("Women's Bottoms", Arrays.asList(
                "Jeans", "Trousers & Slacks", "Skirts", "Shorts", "Leggings & Tights"
        )));

        categoriesList.add(new Category("Dresses & One-Pieces", Arrays.asList(
                "Casual & Sundresses", "Cocktail & Party Dresses", "Formal Gowns & Evening Dresses", "Maxis & Midis", "Jumpsuits", "Rompers", "Overalls"
        )));
    }

    private void setupCategoryChips() {
        chipGroupCategories.removeAllViews();
        for (int i = 0; i < categoriesList.size(); i++) {
            Category category = categoriesList.get(i);
            Chip chip = new Chip(this);
            chip.setText(category.getName());
            chip.setCheckable(true);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    updateItems(category.getItems());
                }
            });

            chipGroupCategories.addView(chip);

            // Select first category by default
            if (i == 0) {
                chip.setChecked(true);
            }
        }
    }

    private void updateItems(List<String> items) {
        itemAdapter.updateData(items);
    }
}