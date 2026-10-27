package com.example.wardrobewizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View addButton = findViewById(R.id.navAdd);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.navWardrobe).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, WardrobeActivity.class)));
    }

    
}