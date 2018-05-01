package com.example.paulnovacovici.portfolio;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by paulnovacovici on 10/25/2017.
 */

public class ProfilePage extends Fragment{
    String following;
    String followers;
    String public_repos;
    String avatar;
    String bio;
    String name;
    String user_name;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (following != null){
            setText();
        }
        getActivity().setTitle("Portfolio");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.portfolio, container, false);
    }

    public void setAll(GithubUser user){
        this.followers = user.getFollowers();
        this.following = user.getFollowing();
        this.public_repos = user.getPublic_Repos();
        this.avatar = user.getAvatar_url();
        this.bio = user.getBio();
        this.user_name = "@" + user.getUserName();
        this.name = user.getName();
        this.bio = user.getBio();

        setText();
    }

    public void setText(){
        ImageView contentImageView = (ImageView) getView().findViewById(R.id.imageView);
        Picasso.with(getContext()).load(avatar).fit().into(contentImageView);
        TextView t = (TextView) getView().findViewById(R.id.num_following);
        t.setText(following);
        t = (TextView) getView().findViewById(R.id.portfolio_user_name);
        t.setText(user_name);
        t = (TextView) getView().findViewById(R.id.portfolio_name);
        t.setText(name);
        t = (TextView) getView().findViewById(R.id.num_followers);
        t.setText(followers);
        t = (TextView) getView().findViewById(R.id.num_repos);
        t.setText(public_repos);
        t = (TextView) getView().findViewById(R.id.bio);
        t.setText(bio);
    }
}
