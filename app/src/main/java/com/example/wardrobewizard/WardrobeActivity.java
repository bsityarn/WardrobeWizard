package com.example.wardrobewizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/** "My Wardrobe" screen - UI only, uses hardcoded dummy items. */
public class WardrobeActivity extends AppCompatActivity {

    private static final String[] FILTERS = {"All", "Tops", "Bottoms", "Jackets", "Shoes", "Accessories"};

    private WardrobeItemAdapter adapter;
    private RecyclerView rvWardrobe;
    private View layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wardrobe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvWardrobe = findViewById(R.id.rvWardrobe);
        layoutEmpty = findViewById(R.id.layoutWardrobeEmpty);

        findViewById(R.id.btnWardrobeBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnWardrobeSort).setOnClickListener(v ->
                Toast.makeText(this, "Sorted by most recent", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btnOpenStylingStudio).setOnClickListener(v ->
                startActivity(new Intent(this, StylingStudioActivity.class)));

        rvWardrobe.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new WardrobeItemAdapter(WardrobeItem.dummyItems(), R.layout.item_wardrobe_tile, this::showItemDialog);
        rvWardrobe.setAdapter(adapter);

        setupChips();
    }

    private void setupChips() {
        ChipGroup group = findViewById(R.id.chipGroupWardrobe);
        for (int i = 0; i < FILTERS.length; i++) {
            String filter = FILTERS[i];
            Chip chip = new Chip(this);
            chip.setText(filter);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipBackgroundColorResource(R.color.wardrobe_chip_bg);
            chip.setTypeface(ResourcesCompat.getFont(this, R.font.didact_gothic));
            chip.setOnCheckedChangeListener((b, isChecked) -> {
                if (isChecked) showItems(WardrobeItem.byCategory(filter));
            });
            group.addView(chip);
            if (i == 0) chip.setChecked(true);
        }
    }

    private void showItems(List<WardrobeItem> items) {
        adapter.updateData(items);
        boolean empty = items.isEmpty();
        layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        rvWardrobe.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private void showItemDialog(WardrobeItem item) {
        View view = getLayoutInflater().inflate(R.layout.dialog_wardrobe_item, null);
        ((EditText) view.findViewById(R.id.etItemBrand)).setText(item.getBrand());
        ((EditText) view.findViewById(R.id.etItemCategory)).setText(item.getCategory());
        ((EditText) view.findViewById(R.id.etItemDescription)).setText(item.getDescription());

        AlertDialog dialog = new MaterialAlertDialogBuilder(this).setView(view).create();
        view.findViewById(R.id.btnItemSave).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btnItemDelete).setOnClickListener(v ->
                new MaterialAlertDialogBuilder(this)
                        .setMessage("Delete this item?")
                        .setPositiveButton("Yes", (d, w) -> dialog.dismiss())
                        .setNegativeButton("No", null)
                        .show());
        dialog.show();
    }
}
