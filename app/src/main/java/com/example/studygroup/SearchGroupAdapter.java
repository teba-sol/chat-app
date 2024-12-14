package com.example.studygroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Group> originalList;
    private List<Group> filteredList;
    private GroupFilter groupFilter;
    private TextView noGroupsFoundView;

    public SearchGroupAdapter(Context context, List<Group> groupsList, TextView noGroupsFoundView) {
        this.context = context;
        this.originalList = new ArrayList<>(groupsList);
        this.filteredList = new ArrayList<>(groupsList); // Initialize with original list
        this.noGroupsFoundView = noGroupsFoundView; // TextView to show "No groups found"
        getFilter();
        Log.d("SearchGroupAdapter", "Adapter initialized with " + groupsList.size() + " groups.");
    }

    public void updateOriginalList(List<Group> groupsList) {
        this.originalList = new ArrayList<>(groupsList);
        Log.d("SearchGroupAdapter", "Original list updated with " + groupsList.size() + " groups.");
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.groupName = convertView.findViewById(R.id.group_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Group group = (Group) getItem(position);
        if (group != null) {
            viewHolder.groupName.setText(group.getGroupName());
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (groupFilter == null) {
            groupFilter = new GroupFilter();
        }
        return groupFilter;
    }

    private static class ViewHolder {
        TextView groupName;
    }

    private class GroupFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("SearchGroupAdapter", "Performing filtering with constraint: " + constraint);
            FilterResults results = new FilterResults();
            List<Group> filteredGroups = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredGroups.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Group group : originalList) {
                    if (group.getGroupName().toLowerCase().contains(filterPattern)) {
                        filteredGroups.add(group);
                    }
                }
            }

            results.values = filteredGroups;
            results.count = filteredGroups.size();
            Log.d("SearchGroupAdapter", "Filtered list size: " + results.count);

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            filteredList.addAll((List<Group>) results.values);
            notifyDataSetChanged();
            Log.d("SearchGroupAdapter", "publishResults: Updated filtered list size: " + filteredList.size());

            // Show or hide "No groups found" TextView based on filteredList size
            if (filteredList.isEmpty()) {
                noGroupsFoundView.setVisibility(View.VISIBLE);
            } else {
                noGroupsFoundView.setVisibility(View.GONE);
            }
        }
    }
}
