package com.moutamid.hbdresidentsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.moutamid.hbdresidentsadmin.adapters.ChatAdapter;
import com.moutamid.hbdresidentsadmin.databinding.ActivityChatBinding;
import com.moutamid.hbdresidentsadmin.models.ChatModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    Date date;
    String ID, userID, message;
    SimpleDateFormat format;
    ChatAdapter adapterChat;
    ArrayList<ChatModel> chatList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ID = getIntent().getStringExtra("ID");
        userID = getIntent().getStringExtra("userID");
        format = new SimpleDateFormat("HH:mm aa");

        chatList = new ArrayList<>();

        binding.message.setText(message);

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setHasFixedSize(false);

        Constants.databaseReference().child("chats").child(Constants.auth().getCurrentUser().getUid())
                .child(userID)
                .child(ID)
                .addChildEventListener(new ChildEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){
                            ChatModel conversation = snapshot.getValue(ChatModel.class);
                            chatList.add(conversation);
                            chatList.sort(Comparator.comparing(ChatModel::getTimestamps));
                            adapterChat = new ChatAdapter(ChatActivity.this, chatList);
                            binding.recycler.setAdapter(adapterChat);
                            binding.recycler.scrollToPosition(chatList.size()-1);
                            adapterChat.notifyItemInserted(chatList.size()-1);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.inProgress.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("status", "INP");
            Constants.databaseReference().child("complaints").child(userID)
                    .child(ID).updateChildren(map).addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Status Changed", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        binding.complete.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("status", "COMP");
            Constants.databaseReference().child("complaints").child(userID)
                    .child(ID).updateChildren(map).addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Status Changed", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        binding.send.setOnClickListener(v -> {
            if (!binding.message.getText().toString().isEmpty()){
                String msg = binding.message.getText().toString();
                //binding.message.setText("");
                date = new Date();
                ChatModel conversation = new ChatModel(
                        msg,
                        Constants.auth().getCurrentUser().getUid(),
                        date.getTime()
                );
                Constants.databaseReference().child("chats").child(Constants.auth().getCurrentUser().getUid())
                        .child(userID)
                        .child(ID)
                        .push()
                        .setValue(conversation)
                        .addOnSuccessListener(unused -> {
                            ReciverSide();
                        }).addOnFailureListener(e -> {

                        });
            }
        });
    }

    private void ReciverSide() {
        String msg = binding.message.getText().toString();
        date = new Date();
        ChatModel conversation = new ChatModel(
                msg,
                Constants.auth().getCurrentUser().getUid(),
                date.getTime()
        );
        Constants.databaseReference().child("chats").child(userID)
                .child(Constants.auth().getCurrentUser().getUid())
                .child(ID)
                .push()
                .setValue(conversation)
                .addOnSuccessListener(unused -> {
                    binding.message.setText("");
                }).addOnFailureListener(e -> {

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