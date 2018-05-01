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

public class repopage extends Fragment{
    ArrayList<RepoObject> repos;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (repos != null) {
            RepoAdapter adapter = new RepoAdapter(getActivity(), repos);
            ListView listView = (ListView) view.findViewById(R.id.lvItems);
            listView.setAdapter(adapter);
            getActivity().setTitle("Repositories");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.repositories, container, false);
    }

    public void setAdapter(ArrayList<RepoObject> repos){
        this.repos = repos;
    }
}
