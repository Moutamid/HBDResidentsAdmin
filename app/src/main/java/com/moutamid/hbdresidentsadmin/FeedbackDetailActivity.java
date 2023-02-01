package com.moutamid.hbdresidentsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.hbdresidentsadmin.databinding.ActivityFeedbackDetailBinding;
import com.moutamid.hbdresidentsadmin.models.UserModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FeedbackDetailActivity extends AppCompatActivity {
    ActivityFeedbackDetailBinding binding;
    String ID, userID, status, image, title, desc;
    long date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ID = getIntent().getStringExtra("ID");
        userID = getIntent().getStringExtra("userID");
        status = getIntent().getStringExtra("status");
        image = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        date = getIntent().getLongExtra("date", 0);

        binding.title.setText(title);
        binding.desc.setText(desc);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aa");
        String mdate = dateFormat.format(date);
        String time = timeFormat.format(date);
        binding.date.setText(mdate + ", " + time.toUpperCase(Locale.ROOT));

        Constants.databaseReference().child("users").child(userID).get().addOnSuccessListener(dataSnapshot -> {
            UserModel userModel = dataSnapshot.getValue(UserModel.class);
            binding.user.setText(userModel.getName());
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}