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

public class FollowingPage extends Fragment{
    ArrayList<FollowerObject> following;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (following != null) {
            FollowingAdapter adapter = new FollowingAdapter(getActivity(), following);
            ListView listView = (ListView) view.findViewById(R.id.following_lvItems);
            listView.setAdapter(adapter);
            getActivity().setTitle("Repositories");
        }

        getActivity().setTitle("Following");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.following, container, false);
    }

    public void setArrayList(ArrayList<FollowerObject> following) {
        this.following = following;
    }
}
