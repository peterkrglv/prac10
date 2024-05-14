package com.example.prac10;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prac10.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        //Set text hello username if shared pref exists
        sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        if (!name.equals("")) {
            binding.textView.setText("Hello, " + name + "!");
        }

        setContentView(binding.getRoot());

        binding.buttonAddUser.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            User user = new User(Integer.valueOf(binding.editTextPhoneNumber.getText().toString()),
                    binding.editTextName.getText().toString(),
                    binding.editTextGender.getText().toString(),
                    binding.editTextAge.getText().toString(),
                    binding.editTextEmail.getText().toString());
            boolean success = db.addUser(user);
            if (success) {
                binding.textView.setText("Hello, " + user.getName() + "!");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", user.getName());
                editor.apply();
            } else {
                binding.textViewResult.setText("Failed to add user");
            }
        });

        binding.buttonEditUser.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            User user = new User(Integer.valueOf(binding.editTextPhoneNumber.getText().toString()),
                    binding.editTextName.getText().toString(),
                    binding.editTextGender.getText().toString(),
                    binding.editTextAge.getText().toString(),
                    binding.editTextEmail.getText().toString());
            boolean success = db.updateUser(user);
            if (success) {
                binding.textView.setText("Hello, " + user.getName() + "!");
                binding.textViewResult.setText("User " + user.getName() +  " edited successfully");
            } else {
                binding.textViewResult.setText("Failed to edit user");
            }
        });
        binding.buttonDeleteUser.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            boolean success = db.deleteUser(binding.editTextPhoneNumber.getText().toString());
            if (success) {
                binding.textViewResult.setText("User deleted successfully");
            } else {
                binding.textViewResult.setText("Failed to delete user");
            }
        });
        binding.buttonSearchUser.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            User user = db.findUser(binding.editTextPhoneNumber.getText().toString());
            if (user != null) {
                binding.textView.setText("Hello, " + user.getName() + "!");
                binding.textViewResult.setText("User " + user.getName() + " found successfully" +
                        "\nName: " + user.getName() +
                        "\nEmail: " + user.getEmail() +
                        "\nPhone: " + user.getPhone() +
                        "\nGender: " + user.getGender() +
                        "\nAge: " + user.getAge());
            } else {
                binding.textViewResult.setText("User not found");
            }
        });
    }
}