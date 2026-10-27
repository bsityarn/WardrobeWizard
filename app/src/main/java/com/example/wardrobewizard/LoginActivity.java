
package com.example.wardrobewizard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Sign up link
        TextView textViewSignUp = findViewById(R.id.textViewSignUp);

        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );
            startActivity(intent);
        });

        // Login button
        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    MainActivity.class
            );
            startActivity(intent);
        });
    }
}
