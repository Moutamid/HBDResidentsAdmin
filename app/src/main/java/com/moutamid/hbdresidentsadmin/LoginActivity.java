package com.moutamid.hbdresidentsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.moutamid.hbdresidentsadmin.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // admin.hbd@gmail.com
        // 12345678

        binding.login.setOnClickListener(v -> {
            if (valid()){
                Constants.auth().signInWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                   startActivity(new Intent(LoginActivity.this, MainActivity.class));
                   finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private boolean valid() {

        if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.getEditText().setError("Email is Required*");
            binding.email.getEditText().requestFocus();
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()){
            binding.password.getEditText().setError("Password is Required*");
            binding.password.getEditText().requestFocus();
            return false;
        }

        return true;
    }
}