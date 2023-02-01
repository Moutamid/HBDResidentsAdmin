package com.moutamid.hbdresidentsadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.hbdresidentsadmin.ChatActivity;
import com.moutamid.hbdresidentsadmin.ComplaintDetailActivity;
import com.moutamid.hbdresidentsadmin.Constants;
import com.moutamid.hbdresidentsadmin.FeedbackDetailActivity;
import com.moutamid.hbdresidentsadmin.R;
import com.moutamid.hbdresidentsadmin.models.ComplaintModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class UrgentAdapter extends RecyclerView.Adapter<UrgentAdapter.ComplaintVH> {
    Context context;
    ArrayList<ComplaintModel> list;

    public UrgentAdapter(Context context, ArrayList<ComplaintModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ComplaintVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.urgent_complaint, parent, false);
        return new ComplaintVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintVH holder, int position) {
        ComplaintModel model = list.get(holder.getAdapterPosition());

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());

        if (model.getStatus().equals("PEN")) {
            holder.status.setText("Pending");
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.dark));
        } else if (model.getStatus().equals("INP")){
            holder.status.setText("In-Progress");
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));
            holder.checked.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.GONE);
        } else {
            holder.status.setText("Resolved");
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.primary));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aa");
        String date = dateFormat.format(model.getTimestamp());
        String time = timeFormat.format(model.getTimestamp());
        holder.date.setText(date + ", " + time.toUpperCase(Locale.ROOT));

        holder.checked.setOnClickListener(v -> {
            Intent i = new Intent(context, ChatActivity.class);
            i.putExtra("ID", model.getId());
            i.putExtra("userID", model.getUserID());
            context.startActivity(i);
        });

        holder.cancel.setOnClickListener(v -> {
            Constants.databaseReference().child("complaints").child(model.getUserID())
                    .child(model.getId())
                    .removeValue()
                    .addOnSuccessListener(unused -> {
                        notifyDataSetChanged();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ComplaintVH extends RecyclerView.ViewHolder{
        TextView title, desc, date, status;
        CardView statusCard, checked, cancel;
        public ComplaintVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            desc = itemView.findViewById(R.id.desc);
            status = itemView.findViewById(R.id.statusTitle);
            statusCard = itemView.findViewById(R.id.status);
            checked = itemView.findViewById(R.id.check);
            cancel = itemView.findViewById(R.id.cancel);
        }
    }
}
