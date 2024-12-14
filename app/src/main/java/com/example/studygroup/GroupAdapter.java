package com.example.studygroup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<Group> {
    private Context mContext;
    private ArrayList<Group> mGroups;
    private String currentUserEmail; // User's email for identification
    private AvailableGroupsActivity mActivity; // Reference to activity

    public GroupAdapter(Context context, ArrayList<Group> groups, String userEmail, AvailableGroupsActivity activity) {
        super(context, 0, groups);
        mContext = context;
        mGroups = groups;
        currentUserEmail = userEmail;
        mActivity = activity; // Assign the activity instance
    }

    public void setGroups(ArrayList<Group> groups) {
        mGroups = groups;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView textViewGroupName;
        Button buttonDetails;
        Button buttonJoin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.customelistview, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewGroupName = convertView.findViewById(R.id.textViewGroupName);
            viewHolder.buttonDetails = convertView.findViewById(R.id.buttondetail);
            viewHolder.buttonJoin = convertView.findViewById(R.id.buttonJoin);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Group currentGroup = mGroups.get(position);

        // Set group name
        viewHolder.textViewGroupName.setText(currentGroup.getGroupName());

        // Define a unique key for the group joined status for the current user
        String joinStatusKey = currentUserEmail + "_" + currentGroup.getGroupName();

        // Set details button
        viewHolder.buttonDetails.setOnClickListener(v -> {
            // Handle details button click
            Intent intent = new Intent(mContext, groupdetials.class);
            intent.putExtra("groupId", currentGroup.getGroupName());
            intent.putExtra("groupName", currentGroup.getGroupName());
            intent.putExtra("groupFocus", currentGroup.getGroupFocus());
            intent.putExtra("groupAdmin", currentGroup.getGroupAdmin());
            mContext.startActivity(intent);
        });

        // Check if the group is already joined by current user
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("joinedGroups", Context.MODE_PRIVATE);
        boolean isJoined = sharedPreferences.getBoolean(joinStatusKey, false);

        if (isJoined) {
            viewHolder.buttonJoin.setText("Joined");
            viewHolder.buttonJoin.setEnabled(false);
        } else {
            viewHolder.buttonJoin.setText("Join");
            viewHolder.buttonJoin.setEnabled(true);
            viewHolder.buttonJoin.setOnClickListener(v -> {
                // Handle join button click
                if (mActivity != null) {
                    mActivity.joinGroup(currentGroup.getGroupName()); // Call joinGroup from activity
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(joinStatusKey, true);
                editor.apply();

                // Change button text to "Joined"
                viewHolder.buttonJoin.setText("Joined");
                viewHolder.buttonJoin.setEnabled(false);

                Toast.makeText(mContext, "Joined group: " + currentGroup.getGroupName(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
