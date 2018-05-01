package com.example.paulnovacovici.portfolio;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by paulnovacovici on 10/25/2017.
 */

public class FollowersPage extends Fragment{
    ArrayList<FollowerObject> followers;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (followers != null) {
            FollowersAdapter adapter = new FollowersAdapter(getActivity(), followers);
            ListView listView = (ListView) view.findViewById(R.id.followers_lvItems);
            listView.setAdapter(adapter);
            getActivity().setTitle("Repositories");
        }
        getActivity().setTitle("Followers");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.followers, container, false);
    }

    public void setArrayList(ArrayList<FollowerObject> followers) {
        this.followers = followers;
    }
}
