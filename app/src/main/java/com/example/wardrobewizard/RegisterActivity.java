
package com.example.wardrobewizard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextFirstName, editTextLastName;
    EditText editTextBirthday, editTextEmail, editTextPassword;
    Spinner spinnerGender;
    Button buttonRegister;
    TextView textViewLogIn;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();

        // Connect XML elements
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        spinnerGender = findViewById(R.id.spinnerGender);

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


        // Gender picker
        String[] genderOptions = {"Select Gender", "Male", "Female"};

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                genderOptions
        );

        genderAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerGender.setAdapter(genderAdapter);
        // Register button
        buttonRegister.setOnClickListener(v -> {
            String firstName = editTextFirstName.getText().toString().trim();
            String lastName = editTextLastName.getText().toString().trim();
            String birthday = editTextBirthday.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem() != null ? spinnerGender.getSelectedItem().toString() : "";
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString();

            if (firstName.isEmpty() ||
                    lastName.isEmpty() ||
                    birthday.isEmpty() ||
                    gender.isEmpty() ||
                    gender.equals("Select Gender") ||
                    email.isEmpty() ||
                    password.isEmpty()) {

                Toast.makeText(
                        RegisterActivity.this,
                        "Please fill in all fields.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            buttonRegister.setEnabled(false);

            // Read check: Verify if email is already registered
            db.collection(FirestoreReferences.USERS_COLLECTION)
                    .whereEqualTo(FirestoreReferences.EMAIL_FIELD, email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            buttonRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "An account with this email already exists.", Toast.LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            // Create: Save new user object in Firestore
                            User newUser = new User(firstName, lastName, birthday, gender, email, password);
                            db.collection(FirestoreReferences.USERS_COLLECTION)
                                    .add(newUser)
                                    .addOnSuccessListener(documentReference -> {
                                        // Store generated Document ID into the user object
                                        documentReference.update("id", documentReference.getId());
                                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        buttonRegister.setEnabled(true);
                                        Toast.makeText(RegisterActivity.this, "Failed to register: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            buttonRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Error: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Return to Login screen
        textViewLogIn.setOnClickListener(v -> finish());
    }
}
