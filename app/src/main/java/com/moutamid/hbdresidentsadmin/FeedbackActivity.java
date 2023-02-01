package com.moutamid.hbdresidentsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.hbdresidentsadmin.adapters.ComplaintAdapter;
import com.moutamid.hbdresidentsadmin.databinding.ActivityFeedbackBinding;
import com.moutamid.hbdresidentsadmin.models.ComplaintModel;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {
    ActivityFeedbackBinding binding;
    ArrayList<ComplaintModel> list;
    ComplaintAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        list = new ArrayList<>();

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        Constants.databaseReference().child("complaints")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                ComplaintModel model = snapshot1.getValue(ComplaintModel.class);
                                if (model.getType().equals("FEED")) {
                                    list.add(model);
                                }
                            }
                        }
                        adapter = new ComplaintAdapter(FeedbackActivity.this, list);
                        binding.recycler.setAdapter(adapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(FeedbackActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}