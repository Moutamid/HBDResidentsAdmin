package com.moutamid.hbdresidentsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.hbdresidentsadmin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.chat.setOnClickListener(view -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

        binding.feedback.setOnClickListener(view -> {
            startActivity(new Intent(this, FeedbackActivity.class));
        });

        binding.complaint.setOnClickListener(view -> {
            startActivity(new Intent(this, ComplaintActivity.class));
        });

        binding.Resolvedcomplaint.setOnClickListener(view -> {
            startActivity(new Intent(this, ResolvedActivity.class));
        });


    }
}