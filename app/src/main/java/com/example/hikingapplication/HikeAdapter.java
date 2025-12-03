package com.example.hikingapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private List<Hike> hikeList;
    private OnHikeClickListener listener;

    public interface OnHikeClickListener {
        void onEdit(Hike hike);
        void onDelete(Hike hike);
        void onViewObservations(Hike hike);
    }

    public HikeAdapter(List<Hike> hikeList, OnHikeClickListener listener) {
        this.hikeList = hikeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hike_item, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikeList.get(position);

        holder.textName.setText(hike.name);
        holder.textLocation.setText("Location: " + hike.location);
        holder.textDate.setText("Date: " + hike.date);
        holder.textLength.setText("Length: " + hike.length + " km");
        holder.textDifficulty.setText("Difficulty: " + hike.difficulty);
        holder.textDescription.setText("Description: " + (hike.description.isEmpty() ? "None" : hike.description));
        holder.textParking.setText("Parking: " + (hike.park ? "Yes" : "No"));
        holder.textChildren.setText("Children: " + (hike.children ? "Yes" : "No"));

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(hike));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(hike));
        holder.btnViewObs.setOnClickListener(v -> listener.onViewObservations(hike));
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    public static class HikeViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textLocation, textDate, textLength, textDifficulty, textDescription, textParking, textChildren;
        Button btnEdit, btnDelete, btnViewObs;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.itemName);
            textLocation = itemView.findViewById(R.id.itemLocation);
            textDate = itemView.findViewById(R.id.itemDate);
            textLength = itemView.findViewById(R.id.itemLength);
            textDifficulty = itemView.findViewById(R.id.itemDifficulty);
            textDescription = itemView.findViewById(R.id.itemDescription);
            textParking = itemView.findViewById(R.id.itemParking);
            textChildren = itemView.findViewById(R.id.itemChildren);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnViewObs = itemView.findViewById(R.id.btnViewObs);
        }

    }

    public void updateList(List<Hike> newList) {
        this.hikeList = newList;
        notifyDataSetChanged();
    }
}
