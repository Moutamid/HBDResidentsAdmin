package com.moutamid.hbdresidentsadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.hbdresidentsadmin.ComplaintDetailActivity;
import com.moutamid.hbdresidentsadmin.FeedbackDetailActivity;
import com.moutamid.hbdresidentsadmin.R;
import com.moutamid.hbdresidentsadmin.models.ComplaintModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintVH> {
    Context context;
    ArrayList<ComplaintModel> list;

    public ComplaintAdapter(Context context, ArrayList<ComplaintModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ComplaintVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complaint_card, parent, false);
        return new ComplaintVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintVH holder, int position) {
        ComplaintModel model = list.get(holder.getAdapterPosition());

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());

        if (model.getStatus().equals("PEN")){
            holder.status.setText("Pending");
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.dark));
        } else if (model.getStatus().equals("INP")){
            holder.status.setText("In-Progress");
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));
        } else {
            holder.status.setText("Resolved");
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.primary));
        }

        if (model.getType().equals("FEED")){
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aa");
        String date = dateFormat.format(model.getTimestamp());
        String time = timeFormat.format(model.getTimestamp());
        holder.date.setText(date + ", " + time.toUpperCase(Locale.ROOT));

        holder.itemView.setOnClickListener(v -> {
            if (model.getType().equals("COMP")) {
                Intent i = new Intent(context, ComplaintDetailActivity.class);
                i.putExtra("ID", model.getId());
                i.putExtra("userID", model.getUserID());
                i.putExtra("title", model.getTitle());
                i.putExtra("desc", model.getDescription());
                i.putExtra("date", model.getTimestamp());
                i.putExtra("image", model.getImage());
                i.putExtra("status", model.getStatus());
                i.putExtra("urgent", model.isUrgent());
                context.startActivity(i);
            } else {
                Intent i = new Intent(context, FeedbackDetailActivity.class);
                i.putExtra("ID", model.getId());
                i.putExtra("userID", model.getUserID());
                i.putExtra("title", model.getTitle());
                i.putExtra("desc", model.getDescription());
                i.putExtra("date", model.getTimestamp());
                i.putExtra("image", model.getImage());
                i.putExtra("status", model.getStatus());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ComplaintVH extends RecyclerView.ViewHolder{
        TextView title, desc, date, status;
        CardView statusCard;
        public ComplaintVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            desc = itemView.findViewById(R.id.desc);
            status = itemView.findViewById(R.id.statusTitle);
            statusCard = itemView.findViewById(R.id.status);
        }
    }
}
