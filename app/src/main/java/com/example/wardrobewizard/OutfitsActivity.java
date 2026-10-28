package com.example.wardrobewizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutfitsActivity extends AppCompatActivity {

    private static final String[] FOLDERS = {
            "All Outfits", "Formal Attire", "Beach Outfits", "Winter Vibes", "Fitness Outfits"
    };

    private final List<Outfit> outfits = new ArrayList<>();
    private OutfitAdapter adapter;
    private TextView emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_outfits);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        seedOutfits();
        emptyState = findViewById(R.id.tvEmptyOutfits);

        RecyclerView recycler = findViewById(R.id.rvOutfits);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new OutfitAdapter(outfits);
        recycler.setAdapter(adapter);

        setupFolders();
        findViewById(R.id.btnOutfitsBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCreateFolder).setOnClickListener(v -> showCreateFolderDialog());
    }

    private void seedOutfits() {
        outfits.clear();
        outfits.add(new Outfit(R.drawable.outfit1, "Formal Attire"));
        outfits.add(new Outfit(R.drawable.outfit2, "Formal Attire"));
        outfits.add(new Outfit(R.drawable.outfit3, "Beach Outfits"));
        outfits.add(new Outfit(R.drawable.outfit4, "Winter Vibes"));
        outfits.add(new Outfit(R.drawable.outfit5, "Beach Outfits"));
        outfits.add(new Outfit(R.drawable.outfit6, "Beach Outfits"));
        outfits.add(new Outfit(R.drawable.outfit7, "Winter Vibes"));
        outfits.add(new Outfit(R.drawable.outfit8, "Fitness Outfits"));
    }

    private void setupFolders() {
        ChipGroup group = findViewById(R.id.chipGroupOutfits);
        group.removeAllViews();

        for (int i = 0; i < FOLDERS.length; i++) {
            final String folder = FOLDERS[i];
            Chip chip = new Chip(this);
            chip.setText(folder);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipBackgroundColorResource(R.color.wardrobe_chip_bg);
            chip.setTypeface(androidx.core.content.res.ResourcesCompat.getFont(this, R.font.didact_gothic));
            chip.setOnCheckedChangeListener((button, checked) -> {
                if (checked) filterFolder(folder);
            });

            if (i > 0) {
                chip.setOnLongClickListener(v -> {
                    showRenameFolderDialog(folder);
                    return true;
                });
            }

            group.addView(chip);
            if (i == 0) chip.setChecked(true);
        }
    }

    private void filterFolder(String folder) {
        List<Outfit> filtered = new ArrayList<>();
        if (folder.equals("All Outfits")) {
            filtered.addAll(outfits);
        } else {
            for (Outfit outfit : outfits) {
                if (outfit.folder.equals(folder)) filtered.add(outfit);
            }
        }
        adapter.setData(filtered);
        emptyState.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showCreateFolderDialog() {
        EditText input = new EditText(this);
        input.setHint("Folder name");
        int pad = (int) (22 * getResources().getDisplayMetrics().density);
        input.setPadding(pad, 0, pad, 0);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Create new folder")
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Create", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        Toast.makeText(this, "Folder \"" + name + "\" created (UI only)", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showRenameFolderDialog(String folder) {
        EditText input = new EditText(this);
        input.setText(folder);
        input.setSelectAllOnFocus(true);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Edit folder name")
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) ->
                        Toast.makeText(this, "Folder renamed (UI only)", Toast.LENGTH_SHORT).show())
                .show();
    }

    private void openOutfitInStylingStudio(Outfit outfit) {
        Intent intent = new Intent(OutfitsActivity.this, StylingStudioActivity.class);
        // UI-only, pass the selected outfit image so the Styling Studio knows which saved outfit was tapped. No persistence rn.
        intent.putExtra("outfit_image_res", outfit.imageRes);
        startActivity(intent);
    }

    private void showOutfitMenu(Outfit outfit) {
        String[] options = {"Move to Folder", "Delete Outfit"};
        new MaterialAlertDialogBuilder(this)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showMoveDialog(outfit);
                    } else {
                        Toast.makeText(this, "Delete selected (UI only)", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showMoveDialog(Outfit outfit) {
        String[] folders = Arrays.copyOfRange(FOLDERS, 1, FOLDERS.length);
        new MaterialAlertDialogBuilder(this)
                .setTitle("Move to Folder")
                .setItems(folders, (dialog, which) ->
                        Toast.makeText(this, "Moved to " + folders[which] + " (UI only)", Toast.LENGTH_SHORT).show())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private static class Outfit {
        final int imageRes;
        final String folder;

        Outfit(int imageRes, String folder) {
            this.imageRes = imageRes;
            this.folder = folder;
        }
    }

    private class OutfitAdapter extends RecyclerView.Adapter<OutfitAdapter.Holder> {
        private final List<Outfit> data = new ArrayList<>();

        OutfitAdapter(List<Outfit> initial) {
            data.addAll(initial);
        }

        void setData(List<Outfit> items) {
            data.clear();
            data.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public Holder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_outfit_card, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Outfit outfit = data.get(position);
            holder.preview.setImageResource(outfit.imageRes);
            holder.folder.setText(outfit.folder);

            holder.itemView.setOnClickListener(v ->
                    openOutfitInStylingStudio(outfit));
            holder.itemView.setOnLongClickListener(v -> {
                showOutfitMenu(outfit);
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            final ImageView preview;
            final TextView folder;

            Holder(View itemView) {
                super(itemView);
                preview = itemView.findViewById(R.id.ivOutfitPreview);
                folder = itemView.findViewById(R.id.tvOutfitFolder);
            }
        }
    }
}
