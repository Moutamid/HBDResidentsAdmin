package com.moutamid.hbdresidentsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

        binding.image.setOnClickListener(v -> showDialog());

    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.show_image_dialog);

        Button close = dialog.findViewById(R.id.close);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.preview_image);
        TextView nothing = dialog.findViewById(R.id.nothing);

        if (image.isEmpty()){
            nothing.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        } else {
            nothing.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }

        Glide.with(FeedbackDetailActivity.this).load(image).into(imageView);

        close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

}