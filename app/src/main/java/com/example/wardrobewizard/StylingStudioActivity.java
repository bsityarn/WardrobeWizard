package com.example.wardrobewizard;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/** Styling Studio screen - UI only (no DB, sensors, or file saving yet). */
public class StylingStudioActivity extends AppCompatActivity {

    private static final String[] TABS = {"Tops", "Bottoms", "Jackets", "Shoes", "Accessories"};
    private static final String[] DUMMY_FOLDERS = {"Work", "Weekend", "Date Night"};

    private WardrobeItemAdapter adapter;
    private FrameLayout canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_styling_studio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        canvas = findViewById(R.id.studioCanvas);

        RecyclerView rv = findViewById(R.id.rvStudioItems);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new WardrobeItemAdapter(WardrobeItem.byCategory(TABS[0]), R.layout.item_studio_thumb,
                item -> placeOnCanvas());
        rv.setAdapter(adapter);
        setupTabs();

        findViewById(R.id.btnStudioBack).setOnClickListener(v -> showDiscardDialog());
        findViewById(R.id.btnStudioSave).setOnClickListener(v -> showSaveDialog());
        findViewById(R.id.btnStudioFolder).setOnClickListener(v -> showFolderDialog());
        findViewById(R.id.btnFitcheckRoulette).setOnClickListener(v -> { /* logic in next iteration */ });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showDiscardDialog();
            }
        });
    }

    private void setupTabs() {
        ChipGroup group = findViewById(R.id.chipGroupStudio);
        for (int i = 0; i < TABS.length; i++) {
            String tab = TABS[i];
            Chip chip = new Chip(this);
            chip.setText(tab);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipBackgroundColorResource(R.color.wardrobe_chip_bg);
            chip.setTypeface(ResourcesCompat.getFont(this, R.font.didact_gothic));
            chip.setOnCheckedChangeListener((b, isChecked) -> {
                if (isChecked) adapter.updateData(WardrobeItem.byCategory(tab));
            });
            group.addView(chip);
            if (i == 0) chip.setChecked(true);
        }
    }

    /** Places a plain placeholder image on the canvas (no drag/lock yet). */
    private void placeOnCanvas() {
        int size = (int) (96 * getResources().getDisplayMetrics().density);
        ImageView image = new ImageView(this);
        image.setImageResource(android.R.drawable.ic_menu_gallery);
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
        int offset = (canvas.getChildCount() % 5) * size / 2;
        lp.leftMargin = offset;
        lp.topMargin = offset;
        canvas.addView(image, lp);
    }

    private void showSaveDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Outfit saved")
                .setMessage("Your outfit has been saved.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showFolderDialog() {
        String[] options = new String[DUMMY_FOLDERS.length + 1];
        System.arraycopy(DUMMY_FOLDERS, 0, options, 0, DUMMY_FOLDERS.length);
        options[DUMMY_FOLDERS.length] = "+ Create new folder";
        new MaterialAlertDialogBuilder(this)
                .setTitle("Add to Folder")
                .setItems(options, (d, which) -> {
                    if (which == DUMMY_FOLDERS.length) showCreateFolderDialog();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showCreateFolderDialog() {
        android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Folder name");
        int pad = (int) (24 * getResources().getDisplayMetrics().density);
        FrameLayout container = new FrameLayout(this);
        container.setPadding(pad, pad / 2, pad, 0);
        container.addView(input);
        new MaterialAlertDialogBuilder(this)
                .setTitle("Create new folder")
                .setView(container)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDiscardDialog() {
        new MaterialAlertDialogBuilder(this)
                .setMessage("Discard outfit?")
                .setPositiveButton("Yes", (d, w) -> finish())
                .setNegativeButton("No", null)
                .show();
    }
}
