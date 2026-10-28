package com.example.wardrobewizard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ItemProcessingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_processing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editTextCategory = findViewById(R.id.editTextCategory);
        if (editTextCategory != null && getIntent().hasExtra("selectedItem")) {
            editTextCategory.setText(getIntent().getStringExtra("selectedItem"));
        }

        Button save = findViewById(R.id.buttonSave);
        Intent intent = new Intent(ItemProcessingActivity.this, AddItemActivity.class);
        save.setOnClickListener(v -> {
            Toast.makeText(ItemProcessingActivity.this, "Item saved!", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}