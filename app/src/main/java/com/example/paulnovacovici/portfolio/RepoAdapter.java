package com.example.paulnovacovici.portfolio;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by paulnovacovici on 10/25/2017.
 */

public class RepoAdapter extends ArrayAdapter<RepoObject> {

    public RepoAdapter(@NonNull Context context, ArrayList<RepoObject> repos) {
        super(context, 0, repos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the data item for this position
        RepoObject repo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView user_name = (TextView) convertView.findViewById(R.id.user_name);
        TextView repo_name = (TextView) convertView.findViewById(R.id.repo_name);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);
        // Populate the data into the template view using the data object
        repo_name.setText(repo.getRepoName());
        user_name.setText(repo.getOwnerName());
        desc.setText(repo.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }
}
