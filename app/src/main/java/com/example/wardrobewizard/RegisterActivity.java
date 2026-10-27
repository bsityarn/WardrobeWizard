
package com.example.wardrobewizard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextFirstName, editTextLastName;
    EditText editTextBirthday, editTextEmail, editTextPassword;
    Button buttonRegister;
    TextView textViewLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Connect XML elements
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogIn = findViewById(R.id.textViewLogIn);

        // Birthday date picker
        editTextBirthday.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            RegisterActivity.this,
                            (view, year, month, dayOfMonth) -> {
                                String birthday = String.format(
                                        Locale.getDefault(),
                                        "%02d/%02d/%04d",
                                        month + 1,
                                        dayOfMonth,
                                        year
                                );

                                editTextBirthday.setText(birthday);
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );

            datePickerDialog.getDatePicker().setMaxDate(
                    System.currentTimeMillis()
            );

            datePickerDialog.show();
        });

        // Register button
        buttonRegister.setOnClickListener(v -> {
            String firstName =
                    editTextFirstName.getText().toString().trim();
            String lastName =
                    editTextLastName.getText().toString().trim();
            String birthday =
                    editTextBirthday.getText().toString().trim();
            String email =
                    editTextEmail.getText().toString().trim();
            String password =
                    editTextPassword.getText().toString();

            if (firstName.isEmpty() ||
                    lastName.isEmpty() ||
                    birthday.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty()) {

                Toast.makeText(
                        RegisterActivity.this,
                        "Please fill in all fields.",
                        Toast.LENGTH_SHORT
                ).show();

            } else {
                Toast.makeText(
                        RegisterActivity.this,
                        "Registration details complete!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // Return to Login screen
        textViewLogIn.setOnClickListener(v -> {
            finish();
        });
    }
}
