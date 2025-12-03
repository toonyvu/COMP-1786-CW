package com.example.hikingapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {

    private ArrayList<Observation> observationList;
    private OnObservationClickListener clickListener;

    public interface OnObservationClickListener {
        void onEdit(Observation observation);
        void onDelete(Observation observation);
    }

    public ObservationAdapter(ArrayList<Observation> observationList,
                              OnObservationClickListener clickListener) {
        this.observationList = observationList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation observation = observationList.get(position);

        holder.titleText.setText(observation.getObservationText());  // <-- Title
        holder.dateText.setText(observation.getTime());
        holder.noteText.setText(observation.getComments());

        holder.editBtn.setOnClickListener(v -> clickListener.onEdit(observation));
        holder.deleteBtn.setOnClickListener(v -> clickListener.onDelete(observation));
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }

    public static class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, dateText, noteText;
        Button editBtn, deleteBtn;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.observationTitle);
            dateText = itemView.findViewById(R.id.observationDate);
            noteText = itemView.findViewById(R.id.observationNote);
            editBtn = itemView.findViewById(R.id.btnEditObservation);
            deleteBtn = itemView.findViewById(R.id.btnDeleteObservation);
        }
    }
}
