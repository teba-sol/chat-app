package com.example.studygroup;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class GroupNameAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> groupNames;

    public GroupNameAdapter(Context context, ArrayList<String> groupNames) {
        super(context, R.layout.group_item, groupNames);
        this.context = context;
        this.groupNames = groupNames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.group_item, parent, false);
        TextView textView = rowView.findViewById(R.id.group_name);
        textView.setText(groupNames.get(position));
        return rowView;
    }
}

