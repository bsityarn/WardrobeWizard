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

        findViewById(R.id.navAdd).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddItemActivity.class)));

        findViewById(R.id.navWardrobe).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, WardrobeActivity.class)));

        findViewById(R.id.navOutfits).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, OutfitsActivity.class)));

        findViewById(R.id.navProfile).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }

    
}