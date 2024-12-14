package com.example.studygroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class
StudyGroupAdapter extends RecyclerView.Adapter<StudyGroupAdapter.ViewHolder> {
    private List<StudyGroup> studyGroups;
    private List<StudyGroup> studyGroupsFiltered;

    public StudyGroupAdapter(List<StudyGroup> studyGroups) {
        this.studyGroups = studyGroups;
        this.studyGroupsFiltered = new ArrayList<>(studyGroups);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StudyGroup studyGroup = studyGroupsFiltered.get(position);
        holder.name.setText(studyGroup.getName());
        holder.description.setText(studyGroup.getDescription());
    }

    @Override
    public int getItemCount() {
        return studyGroupsFiltered.size();
    }

    public void filter(String text) {
        studyGroupsFiltered.clear();
        if (text.isEmpty()) {
            studyGroupsFiltered.addAll(studyGroups);
        } else {
            text = text.toLowerCase();
            for (StudyGroup item : studyGroups) {
                if (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)) {
                    studyGroupsFiltered.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(android.R.id.text1);
            description = view.findViewById(android.R.id.text2);
        }
    }
}
